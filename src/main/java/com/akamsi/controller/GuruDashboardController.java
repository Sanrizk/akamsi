package com.akamsi.controller;

import com.akamsi.entity.Guru;
import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Pengumuman;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruDashboardService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuruDashboardController {

    @Autowired
    private GuruDashboardService guruDashboardService;

    @GetMapping({"/guru", "/guru/dashboard"})
    public String index(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        Long userId = userDetails.getUser().getId();
        Guru guru = guruDashboardService.getGuruProfile(userId).orElse(null);

        String hariIni = guruDashboardService.getHariIniIndo();
        String tanggalLengkap = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID")));

        List<JadwalPelajaran> jadwalHariIni = guruDashboardService.getJadwalHariIni(userId);
        List<JadwalPelajaran> allJadwal = guruDashboardService.getAllJadwalMingguan(userId);
        var kelasAjarList = guruDashboardService.getKelasAjarSummary(userId);
        List<Pengumuman> pengumumanList = guruDashboardService.getPengumumanGuru();

        model.addAttribute("guru", guru);
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("hariIni", hariIni);
        model.addAttribute("tanggalLengkap", tanggalLengkap);
        model.addAttribute("jadwalHariIni", jadwalHariIni);
        model.addAttribute("totalSesiHariIni", jadwalHariIni.size());
        model.addAttribute("totalKelasDiampu", kelasAjarList.size());
        model.addAttribute("totalJadwalMingguan", allJadwal.size());
        model.addAttribute("kelasAjarList", kelasAjarList);
        model.addAttribute("pengumumanList", pengumumanList);

        return "guru/dashboard";
    }
}

