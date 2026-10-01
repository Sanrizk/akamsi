package com.akamsi.controller;

import com.akamsi.dto.MapelOptionDTO;
import com.akamsi.dto.PresensiBatchFormDTO;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.MataPelajaranRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruPresensiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/guru/presensi")
public class GuruPresensiController {

    private final GuruPresensiService guruPresensiService;
    private final JadwalPelajaranRepository jadwalPelajaranRepository;
    private final KelasRepository kelasRepository;
    private final MataPelajaranRepository mapelRepository;

    public GuruPresensiController(GuruPresensiService guruPresensiService, 
                                  JadwalPelajaranRepository jadwalPelajaranRepository,
                                  KelasRepository kelasRepository,
                                  MataPelajaranRepository mapelRepository) {
        this.guruPresensiService = guruPresensiService;
        this.jadwalPelajaranRepository = jadwalPelajaranRepository;
        this.kelasRepository = kelasRepository;
        this.mapelRepository = mapelRepository;
    }

    @GetMapping
    public String viewPresensi(@RequestParam(required = false) Long kelasId,
                               @RequestParam(required = false) Long mapelId,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggal,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        List<Kelas> kelasList = jadwalPelajaranRepository.findDistinctKelasByUserId(userId);
        model.addAttribute("kelasList", kelasList);
        
        LocalDate finalTanggal = (tanggal != null) ? tanggal : LocalDate.now();
        model.addAttribute("selectedTanggal", finalTanggal);

        if (kelasId != null) {
            model.addAttribute("selectedKelasId", kelasId);
            List<MataPelajaran> mapelList = jadwalPelajaranRepository.findDistinctMapelByUserIdAndKelasId(userId, kelasId);
            model.addAttribute("mapelList", mapelList);
            
            if (mapelId != null) {
                model.addAttribute("selectedMapelId", mapelId);
                Kelas selectedKelas = kelasRepository.findById(kelasId).orElse(null);
                MataPelajaran selectedMapel = mapelRepository.findById(mapelId).orElse(null);
                model.addAttribute("selectedKelas", selectedKelas);
                model.addAttribute("selectedMapel", selectedMapel);
                
                PresensiBatchFormDTO formPresensi = guruPresensiService.getPresensiForm(kelasId, mapelId, finalTanggal);
                model.addAttribute("formPresensi", formPresensi);
            }
        }

        return "guru/presensi";
    }

    @ResponseBody
    @GetMapping("/api/mapel-by-kelas")
    public List<MapelOptionDTO> getMapelByKelas(@RequestParam Long kelasId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        return jadwalPelajaranRepository.findDistinctMapelByUserIdAndKelasId(userId, kelasId)
                .stream()
                .map(m -> new MapelOptionDTO(m.getId(), m.getNamaMapel()))
                .collect(Collectors.toList());
    }

    @PostMapping("/save")
    public String savePresensi(@ModelAttribute PresensiBatchFormDTO formPresensi, RedirectAttributes ra) {
        try {
            guruPresensiService.saveBatchPresensi(formPresensi);
            ra.addFlashAttribute("success", "Presensi berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menyimpan presensi: " + e.getMessage());
        }
        return "redirect:/guru/presensi?kelasId=" + formPresensi.getKelasId() + "&mapelId=" + formPresensi.getMapelId() + "&tanggal=" + formPresensi.getTanggal();
    }
}
