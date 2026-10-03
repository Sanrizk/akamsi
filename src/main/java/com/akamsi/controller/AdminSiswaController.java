package com.akamsi.controller;

import com.akamsi.dto.SiswaFormDTO;
import com.akamsi.service.AkademikService;
import com.akamsi.service.SiswaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/siswa")
public class AdminSiswaController {

    private final SiswaService siswaService;
    private final AkademikService akademikService;

    public AdminSiswaController(SiswaService siswaService, AkademikService akademikService) {
        this.siswaService = siswaService;
        this.akademikService = akademikService;
    }

    @GetMapping
    public String viewSiswa(Model model) {
        model.addAttribute("siswaList", siswaService.getAllSiswa());
        model.addAttribute("kelasList", akademikService.getAllKelas());
        return "admin/siswa";
    }

    @PostMapping("/add")
    public String addSiswa(@ModelAttribute SiswaFormDTO dto, RedirectAttributes ra) {
        try {
            siswaService.saveSiswa(dto);
            ra.addFlashAttribute("success", "Data siswa dan akun berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal: " + e.getMessage());
        }
        return "redirect:/admin/siswa";
    }

    @PostMapping("/edit/{id}")
    public String editSiswa(@PathVariable Long id, @ModelAttribute SiswaFormDTO dto, RedirectAttributes ra) {
        try {
            siswaService.updateSiswa(id, dto);
            ra.addFlashAttribute("success", "Data siswa berhasil diperbarui.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal: " + e.getMessage());
        }
        return "redirect:/admin/siswa";
    }

    @PostMapping("/delete/{id}")
    public String deleteSiswa(@PathVariable Long id, RedirectAttributes ra) {
        try {
            siswaService.deleteSiswa(id);
            ra.addFlashAttribute("success", "Siswa dan akun terkait berhasil dihapus.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Gagal menghapus: Data siswa ini tidak dapat dihapus karena masih memiliki riwayat Presensi, Nilai Rapor, atau Tagihan SPP. Hapus data terkait terlebih dahulu.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal: " + e.getMessage());
        }
        return "redirect:/admin/siswa";
    }
}
