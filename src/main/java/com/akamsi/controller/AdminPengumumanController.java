package com.akamsi.controller;

import com.akamsi.entity.Pengumuman;
import com.akamsi.repository.PengumumanRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pengumuman")
public class AdminPengumumanController {

    @Autowired
    private PengumumanRepository pengumumanRepository;

    @GetMapping
    public String index(Model model) {
        List<Pengumuman> pengumumanList = pengumumanRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("pengumumanList", pengumumanList);
        return "admin/pengumuman";
    }

    @PostMapping("/add")
    public String addPengumuman(
            @RequestParam String judul,
            @RequestParam String isi,
            @RequestParam String targetRole,
            RedirectAttributes ra) {
        try {
            Pengumuman p = new Pengumuman();
            p.setJudul(judul.trim());
            p.setIsi(isi.trim());
            p.setTargetRole(targetRole);
            p.setPenulis("Administrator"); // Default admin signature
            
            pengumumanRepository.save(p);
            ra.addFlashAttribute("success", "Pengumuman berhasil diterbitkan!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menambahkan pengumuman: " + e.getMessage());
        }
        return "redirect:/admin/pengumuman";
    }

    @PostMapping("/edit/{id}")
    public String editPengumuman(
            @PathVariable Long id,
            @RequestParam String judul,
            @RequestParam String isi,
            @RequestParam String targetRole,
            RedirectAttributes ra) {
        try {
            Pengumuman p = pengumumanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Data tidak ditemukan"));
            
            p.setJudul(judul.trim());
            p.setIsi(isi.trim());
            p.setTargetRole(targetRole);
            
            pengumumanRepository.save(p);
            ra.addFlashAttribute("success", "Pengumuman berhasil diperbarui!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal memperbarui pengumuman: " + e.getMessage());
        }
        return "redirect:/admin/pengumuman";
    }

    @PostMapping("/delete/{id}")
    public String deletePengumuman(@PathVariable Long id, RedirectAttributes ra) {
        try {
            pengumumanRepository.deleteById(id);
            ra.addFlashAttribute("success", "Pengumuman berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus pengumuman: " + e.getMessage());
        }
        return "redirect:/admin/pengumuman";
    }
}

