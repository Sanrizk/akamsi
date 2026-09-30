package com.akamsi.controller;

import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruNilaiService;
import com.akamsi.service.GuruPresensiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/guru/nilai")
public class GuruNilaiController {

    private final GuruNilaiService nilaiService;
    private final GuruPresensiService presensiService; 
    
    public GuruNilaiController(GuruNilaiService nilaiService, GuruPresensiService presensiService) {
        this.nilaiService = nilaiService;
        this.presensiService = presensiService;
    }

    @GetMapping
    public String viewNilaiPanel(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            model.addAttribute("siswaList", presensiService.getSiswaByGuru(userDetails.getUser()));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "guru/nilai-input";
    }

    @PostMapping("/submit")
    public String submitNilai(@RequestParam Long siswaId,
                              @RequestParam Long mapelId,
                              @RequestParam String semester,
                              @RequestParam Double tugas,
                              @RequestParam Double uts,
                              @RequestParam Double uas) {
                              
        nilaiService.saveOrUpdateNilai(siswaId, mapelId, semester, tugas, uts, uas);
        return "redirect:/guru/nilai";
    }

    @GetMapping("/report/{siswaId}")
    public String viewReportCard(@PathVariable Long siswaId, 
                                 @RequestParam String semester, 
                                 Model model) {
        model.addAttribute("nilaiList", nilaiService.getReportCard(siswaId, semester));
        return "guru/rapor-preview";
    }
}
