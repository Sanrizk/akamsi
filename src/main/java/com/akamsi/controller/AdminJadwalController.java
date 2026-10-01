package com.akamsi.controller;

import com.akamsi.dto.JadwalPelajaranFormDTO;
import com.akamsi.entity.*;
import com.akamsi.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/akademik/jadwal")
public class AdminJadwalController {

    private final JadwalPelajaranRepository jadwalPelajaranRepository;
    private final GuruRepository guruRepository;
    private final KelasRepository kelasRepository;
    private final MataPelajaranRepository mapelRepository;

    public AdminJadwalController(JadwalPelajaranRepository jadwalPelajaranRepository,
                                 GuruRepository guruRepository,
                                 KelasRepository kelasRepository,
                                 MataPelajaranRepository mapelRepository) {
        this.jadwalPelajaranRepository = jadwalPelajaranRepository;
        this.guruRepository = guruRepository;
        this.kelasRepository = kelasRepository;
        this.mapelRepository = mapelRepository;
    }

    @GetMapping
    public String viewJadwal(@RequestParam(required = false) Long kelasId, Model model) {
        model.addAttribute("kelasList", kelasRepository.findAll());
        model.addAttribute("guruList", guruRepository.findAll());
        model.addAttribute("mapelList", mapelRepository.findAll());
        model.addAttribute("hariList", Arrays.asList(Hari.values()));
        
        if (kelasId != null) {
            model.addAttribute("selectedKelasId", kelasId);
            Kelas selectedKelas = kelasRepository.findById(kelasId).orElse(null);
            model.addAttribute("selectedKelas", selectedKelas);
            
            List<JadwalPelajaran> jadwalList = jadwalPelajaranRepository.findAll().stream()
                    .filter(j -> j.getKelas() != null && j.getKelas().getId().equals(kelasId))
                    .collect(Collectors.toList());
            model.addAttribute("jadwalList", jadwalList);
        }

        return "admin/akademik/jadwal";
    }

    @PostMapping("/add")
    public String addJadwal(@ModelAttribute JadwalPelajaranFormDTO dto, RedirectAttributes ra) {
        try {
            JadwalPelajaran jp = new JadwalPelajaran();
            jp.setGuru(guruRepository.findById(dto.getGuruId()).orElseThrow(() -> new RuntimeException("Guru tidak ditemukan")));
            jp.setKelas(kelasRepository.findById(dto.getKelasId()).orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan")));
            jp.setMapel(mapelRepository.findById(dto.getMapelId()).orElseThrow(() -> new RuntimeException("Mapel tidak ditemukan")));
            jp.setHari(dto.getHari());
            jp.setJamMulai(LocalTime.parse(dto.getJamMulai()));
            jp.setJamSelesai(LocalTime.parse(dto.getJamSelesai()));
            
            jadwalPelajaranRepository.save(jp);
            ra.addFlashAttribute("success", "Jadwal pelajaran berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menambahkan jadwal: " + e.getMessage());
        }
        return "redirect:/admin/akademik/jadwal?kelasId=" + dto.getKelasId();
    }

    @PostMapping("/edit/{id}")
    public String editJadwal(@PathVariable Long id, @ModelAttribute JadwalPelajaranFormDTO dto, RedirectAttributes ra) {
        try {
            JadwalPelajaran jp = jadwalPelajaranRepository.findById(id).orElseThrow(() -> new RuntimeException("Jadwal tidak ditemukan"));
            jp.setGuru(guruRepository.findById(dto.getGuruId()).orElseThrow(() -> new RuntimeException("Guru tidak ditemukan")));
            jp.setKelas(kelasRepository.findById(dto.getKelasId()).orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan")));
            jp.setMapel(mapelRepository.findById(dto.getMapelId()).orElseThrow(() -> new RuntimeException("Mapel tidak ditemukan")));
            jp.setHari(dto.getHari());
            jp.setJamMulai(LocalTime.parse(dto.getJamMulai()));
            jp.setJamSelesai(LocalTime.parse(dto.getJamSelesai()));
            
            jadwalPelajaranRepository.save(jp);
            ra.addFlashAttribute("success", "Jadwal pelajaran berhasil diperbarui.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal memperbarui jadwal: " + e.getMessage());
        }
        return "redirect:/admin/akademik/jadwal?kelasId=" + dto.getKelasId();
    }

    @PostMapping("/delete/{id}")
    public String deleteJadwal(@PathVariable Long id, @RequestParam Long kelasId, RedirectAttributes ra) {
        try {
            jadwalPelajaranRepository.deleteById(id);
            ra.addFlashAttribute("success", "Jadwal pelajaran berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal menghapus jadwal: " + e.getMessage());
        }
        return "redirect:/admin/akademik/jadwal?kelasId=" + kelasId;
    }
}
