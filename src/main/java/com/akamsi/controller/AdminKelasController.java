package com.akamsi.controller;

import com.akamsi.entity.Guru;
import com.akamsi.entity.Kelas;
import com.akamsi.repository.GuruRepository;
import com.akamsi.service.AkademikService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/kelas")
public class AdminKelasController {

    @Autowired
    private AkademikService akademikService;

    @Autowired
    private GuruRepository guruRepository;

    @GetMapping
    public String index(Model model) {
        List<Kelas> kelasList = akademikService.getAllKelas();
        List<Guru> guruList = guruRepository.findAll();

        model.addAttribute("kelasList", kelasList);
        model.addAttribute("guruList", guruList);
        return "admin/kelas";
    }

    @PostMapping("/simpan")
    public String simpanKelas(
            @RequestParam("namaKelas") String namaKelas,
            @RequestParam(value = "tahunAjaran", required = false) String tahunAjaran,
            @RequestParam(value = "waliKelasId", required = false) Long waliKelasId,
            RedirectAttributes redirectAttributes) {

        try {
            Kelas kelas = new Kelas();
            kelas.setNamaKelas(namaKelas.trim());
            kelas.setTahunAjaran(tahunAjaran);

            if (waliKelasId != null) {
                Guru wali = guruRepository.findById(waliKelasId).orElse(null);
                kelas.setWaliKelas(wali);
            }

            akademikService.saveKelas(kelas);
            redirectAttributes.addFlashAttribute("success", "Kelas berhasil ditambahkan beserta penugasan Wali Kelas.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menyimpan data kelas: " + e.getMessage());
        }

        return "redirect:/admin/kelas";
    }

    @PostMapping("/update/{id}")
    public String updateKelas(
            @PathVariable("id") Long id,
            @RequestParam("namaKelas") String namaKelas,
            @RequestParam(value = "tahunAjaran", required = false) String tahunAjaran,
            @RequestParam(value = "waliKelasId", required = false) Long waliKelasId,
            RedirectAttributes redirectAttributes) {

        try {
            Kelas kelas = akademikService.findKelasById(id);
            if (kelas == null) {
                redirectAttributes.addFlashAttribute("error", "Kelas tidak ditemukan.");
                return "redirect:/admin/kelas";
            }

            kelas.setNamaKelas(namaKelas.trim());
            kelas.setTahunAjaran(tahunAjaran);

            if (waliKelasId != null) {
                Guru wali = guruRepository.findById(waliKelasId).orElse(null);
                kelas.setWaliKelas(wali);
            } else {
                kelas.setWaliKelas(null);
            }

            akademikService.saveKelas(kelas);
            redirectAttributes.addFlashAttribute("success", "Data kelas dan Wali Kelas berhasil diperbarui.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal memperbarui kelas: " + e.getMessage());
        }

        return "redirect:/admin/kelas";
    }

    @PostMapping("/hapus/{id}")
    public String hapusKelas(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            akademikService.deleteKelas(id);
            redirectAttributes.addFlashAttribute("success", "Kelas berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Kelas tidak dapat dihapus karena masih terkait dengan data siswa atau jadwal: " + e.getMessage());
        }
        return "redirect:/admin/kelas";
    }
}
