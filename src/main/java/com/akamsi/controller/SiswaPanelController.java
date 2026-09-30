package com.akamsi.controller;

import com.akamsi.entity.Siswa;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.SiswaPanelService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/siswa")
public class SiswaPanelController {

    private final SiswaPanelService panelService;

    public SiswaPanelController(SiswaPanelService panelService) {
        this.panelService = panelService;
    }

    // Helper method to retrieve the linked Siswa entity from the logged-in User
    private Siswa getSiswaOrThrow(CustomUserDetails userDetails) {
        return panelService.getSiswaProfile(userDetails.getUser());
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Siswa siswa = getSiswaOrThrow(userDetails);
            model.addAttribute("siswa", siswa);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "siswa/dashboard";
    }

    @GetMapping("/presensi")
    public String viewAttendanceHistory(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Siswa siswa = getSiswaOrThrow(userDetails);
            model.addAttribute("siswa", siswa);
            model.addAttribute("presensiList", panelService.getAttendanceHistory(siswa));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "siswa/presensi-history";
    }

    @GetMapping("/rapor")
    public String viewReportCard(@RequestParam(defaultValue = "Ganjil") String semester,
                                 Model model, 
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Siswa siswa = getSiswaOrThrow(userDetails);
            model.addAttribute("siswa", siswa);
            model.addAttribute("semester", semester);
            model.addAttribute("nilaiList", panelService.getReportCard(siswa, semester));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "siswa/rapor";
    }

    @GetMapping("/spp")
    public String viewSppHistory(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Siswa siswa = getSiswaOrThrow(userDetails);
            model.addAttribute("siswa", siswa);
            model.addAttribute("sppList", panelService.getSppHistory(siswa));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "siswa/spp-status";
    }
}
