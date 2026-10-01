package com.akamsi.controller;

import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.SppService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/spp")
public class AdminSppController {
    private final SppService sppService;

    public AdminSppController(SppService sppService) {
        this.sppService = sppService;
    }

    // READ
    @GetMapping
    public String listSpp(Model model) {
        model.addAttribute("sppList", sppService.getAllPembayaran());
        return "admin/spp-history";
    }

    // PAY (mark as LUNAS)
    @PostMapping("/pay")
    public String processPayment(@RequestParam Long pembayaranId,
                                 @RequestParam String catatan,
                                 @AuthenticationPrincipal CustomUserDetails currentUser,
                                 RedirectAttributes ra) {
        try {
            sppService.processManualPayment(pembayaranId, currentUser.getUser(), catatan);
            ra.addFlashAttribute("success", "Pembayaran SPP berhasil diproses.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/spp";
    }

    // VOID (cancel payment, back to BELUM_LUNAS)
    @PostMapping("/void/{id}")
    public String voidPayment(@PathVariable Long id, RedirectAttributes ra) {
        try {
            sppService.voidPayment(id);
            ra.addFlashAttribute("success", "Pembayaran berhasil dibatalkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/spp";
    }
}
