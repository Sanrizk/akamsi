package com.akamsi.controller;

import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.SppService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/spp")
public class AdminSppController {
    private final SppService sppService;

    public AdminSppController(SppService sppService) {
        this.sppService = sppService;
    }

    @GetMapping
    public String listSpp(Model model) {
        model.addAttribute("sppList", sppService.getAllPembayaran());
        return "admin/spp-history";
    }

    @PostMapping("/pay")
    public String processPayment(@RequestParam Long pembayaranId, 
                                 @RequestParam String catatan,
                                 @AuthenticationPrincipal CustomUserDetails currentUser) {
        
        // currentUser.getUser() fetches the currently logged in Admin from Spring Security context
        sppService.processManualPayment(pembayaranId, currentUser.getUser(), catatan);
        
        return "redirect:/admin/spp";
    }
}
