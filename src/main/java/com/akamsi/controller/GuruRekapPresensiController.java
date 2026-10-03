package com.akamsi.controller;

import com.akamsi.dto.RekapPresensiKelasSummaryDTO;
import com.akamsi.dto.RekapPresensiSiswaDTO;
import com.akamsi.entity.Guru;
import com.akamsi.entity.Kelas;
import com.akamsi.service.AkademikService;
import com.akamsi.repository.GuruRepository;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.RekapPresensiService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/guru/rekap-presensi")
public class GuruRekapPresensiController {

    @Autowired
    private RekapPresensiService rekapPresensiService;

    @Autowired
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private AkademikService akademikService;

    private static final List<String> DAFTAR_BULAN = Arrays.asList(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    );

    @GetMapping
    public String index(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long kelasId,
            @RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun,
            Model model) {

        Long userId = userDetails.getUser().getId();
        Guru guru = guruRepository.findByUserId(userId).orElse(null);

        // Fetch classes taught by this teacher
        List<Kelas> distinctKelas = jadwalPelajaranRepository.findDistinctKelasByUserId(userId);
        if (distinctKelas.isEmpty()) {
            distinctKelas = akademikService.getAllKelas(); // Fallback if general homeroom
        }

        LocalDate now = LocalDate.now();
        int selectedBulan = (bulan != null && bulan >= 1 && bulan <= 12) ? bulan : now.getMonthValue();
        int selectedTahun = (tahun != null) ? tahun : now.getYear();

        Long targetKelasId = (kelasId != null) 
                ? kelasId 
                : (!distinctKelas.isEmpty() ? distinctKelas.get(0).getId() : null);

        List<RekapPresensiSiswaDTO> rekapList = List.of();
        RekapPresensiKelasSummaryDTO summary = new RekapPresensiKelasSummaryDTO(0, 100.0, 0, 0, 0);
        Kelas targetKelas = null;

        if (targetKelasId != null) {
            targetKelas = akademikService.findKelasById(targetKelasId);
            rekapList = rekapPresensiService.getRekapBulananKelas(targetKelasId, selectedBulan, selectedTahun);
            summary = rekapPresensiService.calculateSummary(rekapList);
        }

        model.addAttribute("guru", guru);
        model.addAttribute("kelasList", distinctKelas);
        model.addAttribute("targetKelasId", targetKelasId);
        model.addAttribute("targetKelas", targetKelas);
        model.addAttribute("selectedBulan", selectedBulan);
        model.addAttribute("namaBulan", DAFTAR_BULAN.get(selectedBulan - 1));
        model.addAttribute("selectedTahun", selectedTahun);
        model.addAttribute("daftarBulan", DAFTAR_BULAN);
        model.addAttribute("rekapList", rekapList);
        model.addAttribute("summary", summary);
        model.addAttribute("tanggalCetak", now);

        return "guru/rekap-presensi";
    }
}
