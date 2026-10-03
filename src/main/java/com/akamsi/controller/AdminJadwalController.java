package com.akamsi.controller;

import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.service.AkademikService;
import com.akamsi.service.JadwalGeneratorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/jadwal")
public class AdminJadwalController {

    @Autowired
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @Autowired
    private AkademikService akademikService;

    @Autowired
    private JadwalGeneratorService jadwalGeneratorService;

    @GetMapping
    public String index(
            @RequestParam(required = false) Long kelasId,
            Model model) {

        List<Kelas> kelasList = akademikService.getAllKelas();
        Long selectedKelasId = (kelasId != null) ? kelasId : (!kelasList.isEmpty() ? kelasList.get(0).getId() : null);

        List<JadwalPelajaran> jadwalList = List.of();
        Kelas selectedKelas = null;

        if (selectedKelasId != null) {
            selectedKelas = akademikService.findKelasById(selectedKelasId);
            jadwalList = jadwalPelajaranRepository.findByKelasIdOrderByHariAscJamMulaiAsc(selectedKelasId);
        }

        model.addAttribute("kelasList", kelasList);
        model.addAttribute("selectedKelasId", selectedKelasId);
        model.addAttribute("selectedKelas", selectedKelas);
        model.addAttribute("jadwalList", jadwalList);
        model.addAttribute("totalJadwalSemua", jadwalPelajaranRepository.count());

        return "admin/jadwal";
    }

    @PostMapping("/generate")
    public String generateOtomatis(
            @RequestParam(value = "resetJadwalLama", defaultValue = "true") boolean resetJadwalLama,
            RedirectAttributes redirectAttributes) {

        try {
            int totalGenerated = jadwalGeneratorService.generateJadwalOtomatis(resetJadwalLama);
            redirectAttributes.addFlashAttribute("success",
                    "Generator Berhasil! Sebanyak " + totalGenerated + " sesi mata pelajaran berhasil disusun otomatis bebas bentrok.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Prasyarat belum terpenuhi: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menyusun jadwal otomatis: " + e.getMessage());
        }

        return "redirect:/admin/jadwal";
    }
}
