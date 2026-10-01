package com.akamsi.controller;

import com.akamsi.entity.Kelas;
import com.akamsi.entity.Kurikulum;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.service.AkademikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/akademik")
public class AdminAkademikController {
    private final AkademikService akademikService;

    public AdminAkademikController(AkademikService akademikService) {
        this.akademikService = akademikService;
    }

    // ==================== KURIKULUM ====================
    @GetMapping("/kurikulum")
    public String listKurikulum(Model model) {
        model.addAttribute("kurikulumList", akademikService.getAllKurikulum());
        return "admin/akademik/kurikulum";
    }

    @PostMapping("/kurikulum/add")
    public String addKurikulum(@ModelAttribute Kurikulum kurikulum, RedirectAttributes ra) {
        try {
            akademikService.saveKurikulum(kurikulum);
            ra.addFlashAttribute("success", "Kurikulum berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/kurikulum";
    }

    @PostMapping("/kurikulum/edit/{id}")
    public String editKurikulum(@PathVariable Long id, @ModelAttribute Kurikulum kurikulum, RedirectAttributes ra) {
        try {
            akademikService.updateKurikulum(id, kurikulum);
            ra.addFlashAttribute("success", "Kurikulum berhasil diupdate.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/kurikulum";
    }

    @PostMapping("/kurikulum/delete/{id}")
    public String deleteKurikulum(@PathVariable Long id, RedirectAttributes ra) {
        try {
            akademikService.deleteKurikulum(id);
            ra.addFlashAttribute("success", "Kurikulum berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/kurikulum";
    }

    // ==================== KELAS ====================
    @GetMapping("/kelas")
    public String listKelas(Model model) {
        model.addAttribute("kelasList", akademikService.getAllKelas());
        return "admin/akademik/kelas";
    }

    @PostMapping("/kelas/add")
    public String addKelas(@ModelAttribute Kelas kelas, RedirectAttributes ra) {
        try {
            akademikService.saveKelas(kelas);
            ra.addFlashAttribute("success", "Kelas berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/kelas";
    }

    @PostMapping("/kelas/edit/{id}")
    public String editKelas(@PathVariable Long id,
                            @RequestParam String namaKelas,
                            @RequestParam String tahunAjaran,
                            RedirectAttributes ra) {
        try {
            akademikService.updateKelas(id, namaKelas, tahunAjaran);
            ra.addFlashAttribute("success", "Kelas berhasil diupdate.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/kelas";
    }

    @PostMapping("/kelas/delete/{id}")
    public String deleteKelas(@PathVariable Long id, RedirectAttributes ra) {
        try {
            akademikService.deleteKelas(id);
            ra.addFlashAttribute("success", "Kelas berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/kelas";
    }

    // ==================== MAPEL ====================
    @GetMapping("/mapel")
    public String listMapel(Model model) {
        model.addAttribute("mapelList", akademikService.getAllMapel());
        return "admin/akademik/mapel";
    }

    @PostMapping("/mapel/add")
    public String addMapel(@ModelAttribute MataPelajaran mapel, RedirectAttributes ra) {
        try {
            akademikService.saveMapel(mapel);
            ra.addFlashAttribute("success", "Mata Pelajaran berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/mapel";
    }

    @PostMapping("/mapel/edit/{id}")
    public String editMapel(@PathVariable Long id,
                            @RequestParam String namaMapel,
                            RedirectAttributes ra) {
        try {
            akademikService.updateMapel(id, namaMapel);
            ra.addFlashAttribute("success", "Mata Pelajaran berhasil diupdate.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/mapel";
    }

    @PostMapping("/mapel/delete/{id}")
    public String deleteMapel(@PathVariable Long id, RedirectAttributes ra) {
        try {
            akademikService.deleteMapel(id);
            ra.addFlashAttribute("success", "Mata Pelajaran berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/akademik/mapel";
    }
}
