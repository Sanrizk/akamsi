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

import java.util.Map;

@Controller
@RequestMapping("/siswa")
public class SiswaPanelController {

    private final SiswaPanelService panelService;

    public SiswaPanelController(SiswaPanelService panelService) {
        this.panelService = panelService;
    }

    private Siswa getSiswaOrThrow(CustomUserDetails userDetails) {
        return panelService.getSiswaProfile(userDetails.getUser());
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
