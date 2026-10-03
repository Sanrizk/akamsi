package com.akamsi.controller;

import com.akamsi.dto.PresensiHariIniDTO;
import com.akamsi.service.AdminDashboardService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    @GetMapping({"/admin", "/admin/dashboard"})
    public String index(Model model) {
        LocalDate today = LocalDate.now();
        String tanggalLengkap = today.format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID")));

        long totalSiswa = adminDashboardService.getTotalSiswa();
        long totalGuru = adminDashboardService.getTotalGuru();
        PresensiHariIniDTO presensiHariIni = adminDashboardService.getRekapPresensiHariIni();

        model.addAttribute("tanggalLengkap", tanggalLengkap);
        model.addAttribute("totalSiswa", totalSiswa);
        model.addAttribute("totalGuru", totalGuru);
        model.addAttribute("presensiHariIni", presensiHariIni);

        return "admin/dashboard";
    }
}
