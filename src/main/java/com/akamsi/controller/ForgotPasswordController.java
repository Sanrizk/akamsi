package com.akamsi.controller;

import com.akamsi.dto.SiswaResetPasswordDTO;
import com.akamsi.entity.Role;
import com.akamsi.entity.User;
import com.akamsi.service.AkademikService;
import com.akamsi.service.AuthService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AkademikService akademikService;

    @GetMapping
    public String showStepOnePage(Model model) {
        return "auth/forgot-password-step1";
    }

    @PostMapping("/check")
    public String checkUsername(
            @RequestParam("username") String username,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Silakan masukkan username atau NISN Anda.");
            return "auth/forgot-password-step1";
        }

        Optional<User> userOpt = authService.findUserByUsername(username);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Username atau NISN tidak ditemukan di sistem.");
            return "auth/forgot-password-step1";
        }

        User user = userOpt.get();

        if (user.getRole() != Role.SISWA) {
            model.addAttribute("adminAlert", "Akun terdeteksi sebagai Guru/Admin. Untuk keamanan, silakan hubungi Admin atau Tim IT sekolah untuk reset password.");
            return "auth/forgot-password-step1";
        }

        return "redirect:/forgot-password/verify?username=" + user.getUsername();
    }

    @GetMapping("/verify")
    public String showStepTwoPage(
            @RequestParam("username") String username,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = authService.findUserByUsername(username);
        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.SISWA) {
            redirectAttributes.addFlashAttribute("error", "Akses tidak valid.");
            return "redirect:/forgot-password";
        }

        SiswaResetPasswordDTO dto = new SiswaResetPasswordDTO();
        dto.setUsername(username);
        dto.setNisn(username);

        model.addAttribute("dto", dto);
        model.addAttribute("kelasList", akademikService.getAllKelas());
        return "auth/forgot-password-step2";
    }

    @PostMapping("/reset")
    public String executeReset(
            @ModelAttribute("dto") SiswaResetPasswordDTO dto,
            Model model,
            RedirectAttributes redirectAttributes) {

        boolean success = authService.verifyAndResetSiswaPassword(dto);

        if (success) {
            redirectAttributes.addFlashAttribute("resetSuccessAlert",
                    "Verifikasi Berhasil! Data cocok.\n\nPassword akun telah di-reset ke default (Nomor HP Orang Tua).\nSilakan login menggunakan NISN dan Nomor HP Orang Tua.");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Data verifikasi tidak cocok dengan data siswa di sistem. Silakan periksa kembali NISN, Nama Lengkap, Nama Orang Tua, No HP, dan Kelas Anda.");
            model.addAttribute("kelasList", akademikService.getAllKelas());
            return "auth/forgot-password-step2";
        }
    }
}
