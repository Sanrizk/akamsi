package com.akamsi.controller;

import com.akamsi.entity.Role;
import com.akamsi.entity.User;
import com.akamsi.repository.UserRepository;
import com.akamsi.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<Role> allowedRoles = Arrays.stream(Role.values())
                .filter(r -> r != Role.SISWA)
                .toList();
        model.addAttribute("roles", allowedRoles);
        
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        
        return "admin/users";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String namaLengkap,
                          @RequestParam Role role,
                          RedirectAttributes ra) {
        try {
            if (role == Role.SISWA) {
                throw new RuntimeException("Role SISWA tidak dapat ditambahkan dari menu ini.");
            }
            if (userRepository.findByUsername(username).isPresent()) {
                throw new RuntimeException("Username sudah terdaftar.");
            }
            
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setNamaLengkap(namaLengkap);
            user.setRole(role);
            user.setCreatedAt(LocalDateTime.now());
            
            userRepository.save(user);
            ra.addFlashAttribute("success", "Pengguna berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menambahkan pengguna: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @RequestParam String username,
                           @RequestParam(required = false) String password,
                           @RequestParam String namaLengkap,
                           @RequestParam Role role,
                           RedirectAttributes ra) {
        try {
            if (role == Role.SISWA) {
                throw new RuntimeException("Role SISWA tidak diperbolehkan di menu ini.");
            }
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pengguna tidak ditemukan."));
                    
            if (!user.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
                throw new RuntimeException("Username sudah terdaftar.");
            }
            
            user.setUsername(username);
            user.setNamaLengkap(namaLengkap);
            user.setRole(role);
            
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            
            userRepository.save(user);
            ra.addFlashAttribute("success", "Data pengguna berhasil diperbarui.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal memperbarui pengguna: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, 
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes ra) {
        try {
            if (userDetails.getUser().getId().equals(id)) {
                throw new RuntimeException("Anda tidak dapat menghapus akun Anda sendiri.");
            }
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pengguna tidak ditemukan."));
                    
            userRepository.delete(user);
            ra.addFlashAttribute("success", "Pengguna berhasil dihapus.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Gagal menghapus: Akun ini tidak dapat dihapus karena masih terhubung dengan profil Guru atau Siswa aktif. Silakan hapus profil terkait di menu Master Data terlebih dahulu.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus pengguna: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
