package com.akamsi.controller;

import com.akamsi.dto.SiswaOptionDTO;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.PembayaranSPPRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.AkademikService;
import com.akamsi.service.SppService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/spp")
public class AdminSppController {

    @Autowired
    private SppService sppService;

    @Autowired
    private AkademikService akademikService;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PembayaranSPPRepository pembayaranSPPRepository;

    @GetMapping
    public String index(
            @RequestParam(required = false) Long kelasId,
            @RequestParam(required = false) Long siswaId,
            Model model) {

        model.addAttribute("kelasList", akademikService.getAllKelas());
        model.addAttribute("defaultNominal", SppService.DEFAULT_NOMINAL_SPP);
        model.addAttribute("currentYear", LocalDate.now().getYear());

        if (kelasId != null) {
            model.addAttribute("siswaList", siswaRepository.findByKelasId(kelasId));
        }

        if (siswaId != null) {
            Siswa selectedSiswa = siswaRepository.findById(siswaId).orElse(null);
            model.addAttribute("selectedSiswa", selectedSiswa);

            List<PembayaranSPP> pembayaranList = pembayaranSPPRepository.findBySiswaIdOrderByTahunAsc(siswaId);
            model.addAttribute("pembayaranList", pembayaranList);
        }

        return "admin/spp";
    }

    @GetMapping("/api/siswa-by-kelas")
    @ResponseBody
    public List<SiswaOptionDTO> getSiswaByKelas(@RequestParam Long kelasId) {
        return siswaRepository.findByKelasId(kelasId).stream()
                .map(s -> new SiswaOptionDTO(s.getId(), s.getNisn(), s.getNamaSiswa()))
                .collect(Collectors.toList());
    }

    @PostMapping("/generate")
    public String generateSpp(
            @RequestParam(required = false) Long kelasId,
            @RequestParam Integer tahun,
            @RequestParam BigDecimal nominal,
            RedirectAttributes redirectAttributes) {
        try {
            int count = sppService.generateTagihanSpp(kelasId, tahun, nominal);
            redirectAttributes.addFlashAttribute("success", 
                    "Berhasil membuat " + count + " tagihan SPP untuk tahun " + tahun + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal membuat tagihan SPP: " + e.getMessage());
        }
        return "redirect:/admin/spp";
    }

    @PostMapping("/pay/{id}")
    public String paySpp(
            @PathVariable Long id,
            @RequestParam Long kelasId,
            @RequestParam Long siswaId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            sppService.processManualPayment(id, userDetails.getUser().getId());
            redirectAttributes.addFlashAttribute("success", "Pembayaran SPP berhasil dicatat LUNAS.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal memproses pembayaran: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }

    @PostMapping("/void/{id}")
    public String voidSpp(
            @PathVariable Long id,
            @RequestParam Long kelasId,
            @RequestParam Long siswaId,
            RedirectAttributes redirectAttributes) {
        try {
            sppService.voidPayment(id);
            redirectAttributes.addFlashAttribute("success", "Status pembayaran berhasil dikembalikan ke BELUM LUNAS.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal membatalkan status pembayaran: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }

    @PostMapping("/nonaktifkan/{id}")
    public String nonaktifkanSpp(
            @PathVariable Long id,
            @RequestParam Long kelasId,
            @RequestParam Long siswaId,
            RedirectAttributes redirectAttributes) {
        try {
            sppService.nonaktifkanTagihan(id);
            redirectAttributes.addFlashAttribute("success", "Tagihan berhasil dinonaktifkan (Status: BATAL).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menonaktifkan tagihan: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }

    @PostMapping("/aktifkan/{id}")
    public String aktifkanSpp(
            @PathVariable Long id,
            @RequestParam Long kelasId,
            @RequestParam Long siswaId,
            RedirectAttributes redirectAttributes) {
        try {
            sppService.aktifkanTagihan(id);
            redirectAttributes.addFlashAttribute("success", "Tagihan berhasil diaktifkan kembali.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengaktifkan tagihan: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }

    @PostMapping("/delete/{id}")
    public String deleteSpp(
            @PathVariable Long id,
            @RequestParam Long kelasId,
            @RequestParam Long siswaId,
            RedirectAttributes redirectAttributes) {
        try {
            sppService.hapusTagihan(id);
            redirectAttributes.addFlashAttribute("success", "Data tagihan berhasil dihapus permanen.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus tagihan: " + e.getMessage());
        }
        return "redirect:/admin/spp?kelasId=" + kelasId + "&siswaId=" + siswaId;
    }
}
