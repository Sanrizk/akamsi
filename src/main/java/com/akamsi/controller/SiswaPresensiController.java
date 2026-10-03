package com.akamsi.controller;

import com.akamsi.dto.PresensiMingguanSummaryDTO;
import com.akamsi.dto.SiswaPresensiFilterDTO;
import com.akamsi.entity.Presensi;
import com.akamsi.entity.Siswa;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.AkademikService;
import com.akamsi.service.SiswaDashboardService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/siswa/presensi")
public class SiswaPresensiController {

    @Autowired
    private SiswaDashboardService siswaDashboardService;

    @Autowired
    private AkademikService akademikService;

    @GetMapping
    public String index(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute("filter") SiswaPresensiFilterDTO filter,
            Model model) {

        Long userId = userDetails.getUser().getId();
        Siswa siswa = siswaDashboardService.getSiswaByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Data profil siswa tidak ditemukan"));

        LocalDate today = LocalDate.now();
        if (filter.getStartDate() == null) {
            filter.setStartDate(today.withDayOfMonth(1)); // 1st of current month
        }
        if (filter.getEndDate() == null) {
            filter.setEndDate(today); // Today
        }

        List<Presensi> presensiList = siswaDashboardService.getRiwayatPresensiSiswa(siswa.getId(), filter);
        PresensiMingguanSummaryDTO summary = siswaDashboardService.calculatePresensiSummary(presensiList);

        model.addAttribute("siswa", siswa);
        model.addAttribute("mapelList", akademikService.getAllMapel());
        model.addAttribute("presensiList", presensiList);
        model.addAttribute("summary", summary);
        model.addAttribute("tanggalCetak", today);

        return "siswa/presensi";
    }
}
