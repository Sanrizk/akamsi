package com.akamsi.controller;

import com.akamsi.dto.UbahPasswordDTO;
import com.akamsi.entity.Role;
import com.akamsi.entity.User;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profil")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String showProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User user = userDetails.getUser();
        model.addAttribute("user", user);
        model.addAttribute("ubahPasswordDTO", new UbahPasswordDTO());

        if (user.getRole() == Role.GURU) {
            profileService.getGuruProfile(user.getId()).ifPresent(guru -> model.addAttribute("guru", guru));
        } else if (user.getRole() == Role.SISWA) {
            profileService.getSiswaProfile(user.getId()).ifPresent(siswa -> model.addAttribute("siswa", siswa));
        }

        return "profil/index";
    }

    @PostMapping("/ubah-password")
    public String ubahPassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute("ubahPasswordDTO") UbahPasswordDTO dto,
            RedirectAttributes redirectAttributes) {

        try {
            profileService.ubahPassword(userDetails.getUser().getId(), dto);
            redirectAttributes.addFlashAttribute("success", "Password berhasil diubah. Silakan gunakan password baru pada login berikutnya.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat mengubah password: " + e.getMessage());
        }

        return "redirect:/profil";
    }
}
