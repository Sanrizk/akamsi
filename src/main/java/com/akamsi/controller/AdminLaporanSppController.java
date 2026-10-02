package com.akamsi.controller;

import com.akamsi.dto.LaporanSppFilterDTO;
import com.akamsi.dto.LaporanSppSummaryDTO;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.StatusPembayaran;
import com.akamsi.service.AkademikService;
import com.akamsi.service.LaporanSppService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/laporan-spp")
public class AdminLaporanSppController {

    @Autowired
    private LaporanSppService laporanSppService;

    @Autowired
    private AkademikService akademikService;

    private static final List<String> DAFTAR_BULAN = Arrays.asList(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    );

    @GetMapping
    public String index(
            @ModelAttribute("filter") LaporanSppFilterDTO filter,
            Model model) {

        // Default year to current year if null
        if (filter.getTahun() == null) {
            filter.setTahun(LocalDate.now().getYear());
        }

        List<PembayaranSPP> hasilLaporan = laporanSppService.getFilteredLaporan(filter);
        LaporanSppSummaryDTO summary = laporanSppService.calculateSummary(hasilLaporan);

        model.addAttribute("kelasList", akademikService.getAllKelas());
        model.addAttribute("daftarBulan", DAFTAR_BULAN);
        model.addAttribute("daftarStatus", StatusPembayaran.values());
        model.addAttribute("laporanList", hasilLaporan);
        model.addAttribute("summary", summary);
        model.addAttribute("tanggalCetak", LocalDate.now());

        return "admin/laporan-spp";
    }
}
