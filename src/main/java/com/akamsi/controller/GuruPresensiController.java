package com.akamsi.controller;

import com.akamsi.entity.StatusPresensi;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruPresensiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/guru/presensi")
public class GuruPresensiController {
    
    private final GuruPresensiService presensiService;
    
    public GuruPresensiController(GuruPresensiService presensiService) {
        this.presensiService = presensiService;
    }

    @GetMapping
    public String viewPresensiPanel(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            model.addAttribute("siswaList", presensiService.getSiswaByGuru(userDetails.getUser()));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "guru/presensi";
    }

    @PostMapping("/submit")
    public String submitPresensi(@RequestParam Long siswaId,
                                 @RequestParam LocalDate tanggal,
                                 @RequestParam StatusPresensi status,
                                 @RequestParam(required = false) String keterangan) {
        presensiService.submitPresensi(siswaId, tanggal, status, keterangan);
        return "redirect:/guru/presensi";
    }
    
    @GetMapping("/summary/{siswaId}")
    public String viewSummary(@PathVariable Long siswaId,
                              @RequestParam int month,
                              @RequestParam int year,
                              Model model) {
        model.addAttribute("presensiList", presensiService.getPresensiSummary(siswaId, month, year));
        return "guru/presensi/summary";
    }
}
