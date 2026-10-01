package com.akamsi.controller;

import com.akamsi.entity.User;
import com.akamsi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // READ - list all
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    // CREATE
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes ra) {
        try {
            userService.saveUser(user);
            ra.addFlashAttribute("success", "User berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // UPDATE
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @RequestParam String username,
                           @RequestParam String namaLengkap,
                           @RequestParam String role,
                           @RequestParam(required = false) String password,
                           RedirectAttributes ra) {
        try {
            userService.updateUser(id, username, namaLengkap, role, password);
            ra.addFlashAttribute("success", "User berhasil diupdate.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.deleteUser(id);
            ra.addFlashAttribute("success", "User berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
