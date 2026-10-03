package com.akamsi.controller;

import com.akamsi.dto.PresensiMingguanSummaryDTO;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.Pengumuman;
import com.akamsi.entity.Siswa;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.SiswaDashboardService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiswaDashboardController {

    @Autowired
    private SiswaDashboardService siswaDashboardService;

    @GetMapping({"/siswa", "/siswa/dashboard"})
    public String index(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        Long userId = userDetails.getUser().getId();
        Siswa siswa = siswaDashboardService.getSiswaByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Data profil siswa tidak ditemukan"));

        LocalDate today = LocalDate.now();
        String tanggalLengkap = today.format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID")));

        String bulanAktif = siswaDashboardService.getBulanIndo(today.getMonthValue());
        int tahunAktif = today.getYear();

        Optional<PembayaranSPP> sppAktif = siswaDashboardService.getSppBulanAktif(siswa.getId());
        PresensiMingguanSummaryDTO presensiMingguan = siswaDashboardService.getKehadiranMingguan(siswa.getId());
        List<Pengumuman> pengumumanList = siswaDashboardService.getPengumumanSiswa();

        model.addAttribute("siswa", siswa);
        model.addAttribute("tanggalLengkap", tanggalLengkap);
        model.addAttribute("bulanAktif", bulanAktif);
        model.addAttribute("tahunAktif", tahunAktif);
        model.addAttribute("sppAktif", sppAktif.orElse(null));
        model.addAttribute("presensiMingguan", presensiMingguan);
        model.addAttribute("pengumumanList", pengumumanList);

        return "siswa/dashboard";
    }
}
