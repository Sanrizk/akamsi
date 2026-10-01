package com.akamsi.controller;

import com.akamsi.dto.SiswaOptionDTO;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.PembayaranSPPRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.AkademikService;
import com.akamsi.service.SppService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/spp")
public class AdminSppController {

    private final SppService sppService;
    private final AkademikService akademikService;
    private final SiswaRepository siswaRepository;
    private final PembayaranSPPRepository pembayaranSPPRepository;

    public AdminSppController(SppService sppService, 
                              AkademikService akademikService, 
                              SiswaRepository siswaRepository, 
                              PembayaranSPPRepository pembayaranSPPRepository) {
        this.sppService = sppService;
        this.akademikService = akademikService;
        this.siswaRepository = siswaRepository;
        this.pembayaranSPPRepository = pembayaranSPPRepository;
    }

    @GetMapping
    public String viewSpp(@RequestParam(required = false) Long kelasId,
                          @RequestParam(required = false) Long siswaId,
                          Model model) {
        
        model.addAttribute("kelasList", akademikService.getAllKelas());
        
        if (kelasId != null) {
            model.addAttribute("selectedKelasId", kelasId);
            model.addAttribute("siswaList", siswaRepository.findAll().stream()
                    .filter(s -> s.getKelas() != null && s.getKelas().getId().equals(kelasId))
                    .collect(Collectors.toList()));
        }

        if (siswaId != null) {
            Siswa selectedSiswa = siswaRepository.findById(siswaId).orElse(null);
            model.addAttribute("selectedSiswa", selectedSiswa);
            if (selectedSiswa != null) {
                List<PembayaranSPP> pembayaranList = pembayaranSPPRepository.findAll().stream()
                        .filter(p -> p.getSiswa().getId().equals(siswaId))
                        .collect(Collectors.toList());
                model.addAttribute("pembayaranList", pembayaranList);
            }
        }
        
        return "admin/spp";
    }

    @ResponseBody
    @GetMapping("/api/siswa-by-kelas")
    public List<SiswaOptionDTO> getSiswaByKelas(@RequestParam Long kelasId) {
        return siswaRepository.findAll().stream()
                .filter(s -> s.getKelas() != null && s.getKelas().getId().equals(kelasId))
                .map(s -> new SiswaOptionDTO(s.getId(), s.getNisn(), s.getNamaSiswa()))
                .collect(Collectors.toList());
    }

    @PostMapping("/pay/{id}")
    public String paySpp(@PathVariable Long id,
                         @RequestParam Long kelasId,
                         @RequestParam Long siswaId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         RedirectAttributes ra) {
        try {
            sppService.processManualPayment(id, userDetails.getUser().getId());
            ra.addFlashAttribute("success", "Pembayaran SPP berhasil dicatat.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal mencatat pembayaran: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }

    @PostMapping("/void/{id}")
    public String voidSpp(@PathVariable Long id,
                          @RequestParam Long kelasId,
                          @RequestParam Long siswaId,
                          RedirectAttributes ra) {
        try {
            sppService.voidPayment(id);
            ra.addFlashAttribute("success", "Pembayaran SPP berhasil dibatalkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal membatalkan pembayaran: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }
}
