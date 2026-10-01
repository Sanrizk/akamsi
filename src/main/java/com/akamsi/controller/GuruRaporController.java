package com.akamsi.controller;

import com.akamsi.dto.RaporEditDTO;
import com.akamsi.dto.SiswaOptionDTO;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruRaporService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/guru/rapor")
public class GuruRaporController {

    private final GuruRaporService guruRaporService;
    private final JadwalPelajaranRepository jadwalPelajaranRepository;
    private final SiswaRepository siswaRepository;

    public GuruRaporController(GuruRaporService guruRaporService,
                               JadwalPelajaranRepository jadwalPelajaranRepository,
                               SiswaRepository siswaRepository) {
        this.guruRaporService = guruRaporService;
        this.jadwalPelajaranRepository = jadwalPelajaranRepository;
        this.siswaRepository = siswaRepository;
    }

    @GetMapping
    public String viewRapor(@RequestParam(required = false) Long kelasId,
                            @RequestParam(required = false) Long siswaId,
                            @RequestParam(defaultValue = "1") Integer semester,
                            Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Long userId = userDetails.getUser().getId();
        
        List<Kelas> kelasList = jadwalPelajaranRepository.findDistinctKelasByUserId(userId);
        model.addAttribute("kelasList", kelasList);
        model.addAttribute("selectedSemester", semester);

        if (kelasId != null) {
            model.addAttribute("selectedKelasId", kelasId);
            List<Siswa> siswaList = siswaRepository.findByKelasId(kelasId);
            model.addAttribute("siswaList", siswaList);
            
            if (siswaId != null) {
                model.addAttribute("selectedSiswaId", siswaId);
                
                Siswa selectedSiswa = siswaRepository.findById(siswaId).orElse(null);
                model.addAttribute("selectedSiswa", selectedSiswa);
                
                if (selectedSiswa != null) {
                    List<RaporAkhir> raporList = guruRaporService.getRaporBySiswa(siswaId, semester);
                    model.addAttribute("raporList", raporList);
                    
                    if (!raporList.isEmpty()) {
                        double sum = 0;
                        for (RaporAkhir r : raporList) {
                            sum += r.getNilaiAkhir();
                        }
                        double rataRata = sum / raporList.size();
                        model.addAttribute("rataRataAkhir", Math.round(rataRata * 100.0) / 100.0);
                    }
                }
            }
        }
        
        return "guru/rapor";
    }

    @ResponseBody
    @GetMapping("/api/siswa-by-kelas")
    public List<SiswaOptionDTO> getSiswaByKelas(@RequestParam Long kelasId) {
        return siswaRepository.findByKelasId(kelasId).stream()
                .map(s -> new SiswaOptionDTO(s.getId(), s.getNisn(), s.getNamaSiswa()))
                .collect(Collectors.toList());
    }

    @PostMapping("/kalkulasi")
    public String kalkulasiRapor(@RequestParam Long kelasId,
                                 @RequestParam Long siswaId,
                                 @RequestParam Integer semester,
                                 RedirectAttributes ra) {
        try {
            guruRaporService.kalkulasiRaporSiswa(siswaId, semester);
            ra.addFlashAttribute("success", "Rapor siswa berhasil digenerate dari komponen nilai.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal mengkalkulasi rapor: " + e.getMessage());
        }
        return "redirect:/guru/rapor?kelasId=" + kelasId + "&siswaId=" + siswaId + "&semester=" + semester;
    }

    @PostMapping("/edit")
    public String editRapor(@ModelAttribute RaporEditDTO dto, RedirectAttributes ra) {
        try {
            guruRaporService.updateRapor(dto);
            ra.addFlashAttribute("success", "Penyesuaian nilai rapor berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menyimpan perubahan rapor: " + e.getMessage());
        }
        return "redirect:/guru/rapor?kelasId=" + dto.getKelasId() + "&siswaId=" + dto.getSiswaId() + "&semester=" + dto.getSemester();
    }

    @PostMapping("/reset")
    public String resetRapor(@RequestParam Long kelasId,
                             @RequestParam Long siswaId,
                             @RequestParam Integer semester,
                             RedirectAttributes ra) {
        try {
            guruRaporService.resetRaporSiswa(siswaId, semester);
            ra.addFlashAttribute("success", "Rekap rapor siswa berhasil direset.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal mereset rapor: " + e.getMessage());
        }
        return "redirect:/guru/rapor?kelasId=" + kelasId + "&siswaId=" + siswaId + "&semester=" + semester;
    }
}
