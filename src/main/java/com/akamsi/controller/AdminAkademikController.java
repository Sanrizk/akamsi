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
    public String addKurikulum(@RequestParam String namaKurikulum, @RequestParam(defaultValue = "false") boolean aktif, RedirectAttributes ra) {
        try {
            Kurikulum kurikulum = new Kurikulum();
            kurikulum.setNamaKurikulum(namaKurikulum.trim());
            kurikulum.setAktif(aktif);
            akademikService.saveKurikulum(kurikulum);
            ra.addFlashAttribute("success", "Kurikulum berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menambahkan: " + e.getMessage());
        }
        return "redirect:/admin/akademik/kurikulum";
    }

    @PostMapping("/kurikulum/edit/{id}")
    public String editKurikulum(@PathVariable Long id, 
                                @RequestParam String namaKurikulum, 
                                @RequestParam(defaultValue = "false") boolean aktif, 
                                RedirectAttributes ra) {
        try {
            Kurikulum kurikulum = new Kurikulum();
            kurikulum.setNamaKurikulum(namaKurikulum.trim());
            kurikulum.setAktif(aktif);
            akademikService.updateKurikulum(id, kurikulum);
            ra.addFlashAttribute("success", "Kurikulum berhasil diupdate.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal mengupdate: " + e.getMessage());
        }
        return "redirect:/admin/akademik/kurikulum";
    }

    @PostMapping("/kurikulum/delete/{id}")
    public String deleteKurikulum(@PathVariable Long id, RedirectAttributes ra) {
        try {
            akademikService.deleteKurikulum(id);
            ra.addFlashAttribute("success", "Kurikulum berhasil dihapus.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Gagal menghapus: Kurikulum ini tidak dapat dihapus karena masih digunakan di data Kelas.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus: " + e.getMessage());
        }
        return "redirect:/admin/akademik/kurikulum";
    }

    // ==================== MAPEL ====================
    @GetMapping("/mapel")
    public String listMapel(Model model) {
        model.addAttribute("mapelList", akademikService.getAllMapel());
        return "admin/akademik/mapel";
    }

    @PostMapping("/mapel/add")
    public String addMapel(@RequestParam String namaMapel, RedirectAttributes ra) {
        try {
            MataPelajaran mapel = new MataPelajaran();
            mapel.setNamaMapel(namaMapel.trim());
            akademikService.saveMapel(mapel);
            ra.addFlashAttribute("success", "Mata Pelajaran berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menambahkan: " + e.getMessage());
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
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Gagal menghapus: Mata Pelajaran ini tidak dapat dihapus karena masih digunakan dalam data Jadwal, Presensi, atau Nilai Rapor.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus: " + e.getMessage());
        }
        return "redirect:/admin/akademik/mapel";
    }
}
