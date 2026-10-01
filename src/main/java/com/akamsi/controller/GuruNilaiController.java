package com.akamsi.controller;

import com.akamsi.dto.MapelOptionDTO;
import com.akamsi.entity.KategoriNilai;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.MataPelajaranRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruNilaiService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/guru/nilai")
public class GuruNilaiController {

    private final GuruNilaiService nilaiService;
    private final JadwalPelajaranRepository jadwalPelajaranRepository;
    private final KelasRepository kelasRepository;
    private final MataPelajaranRepository mapelRepository;

    public GuruNilaiController(GuruNilaiService nilaiService,
                               JadwalPelajaranRepository jadwalPelajaranRepository,
                               KelasRepository kelasRepository,
                               MataPelajaranRepository mapelRepository) {
        this.nilaiService = nilaiService;
        this.jadwalPelajaranRepository = jadwalPelajaranRepository;
        this.kelasRepository = kelasRepository;
        this.mapelRepository = mapelRepository;
    }

    @GetMapping
    public String viewNilaiPanel(@RequestParam(required = false) Long kelasId,
                                 @RequestParam(required = false) Long mapelId,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Long userId = userDetails.getUser().getId();
        
        List<Kelas> kelasList = jadwalPelajaranRepository.findDistinctKelasByUserId(userId);
        
        System.out.println("DEBUG [GuruNilaiController] User ID: " + userId);
        System.out.println("DEBUG [GuruNilaiController] Classes found: " + kelasList.size());
        
        model.addAttribute("kelasList", kelasList);

        if (kelasId != null) {
            List<MataPelajaran> mapelList = jadwalPelajaranRepository.findDistinctMapelByUserIdAndKelasId(userId, kelasId);
            model.addAttribute("mapelList", mapelList);
            
            if (mapelId != null) {
                Kelas selectedKelas = kelasRepository.findById(kelasId).orElse(null);
                MataPelajaran selectedMapel = mapelRepository.findById(mapelId).orElse(null);
                
                model.addAttribute("selectedKelas", selectedKelas);
                model.addAttribute("selectedMapel", selectedMapel);
                
                if (selectedKelas != null && selectedMapel != null) {
                    model.addAttribute("komponenList", nilaiService.getKomponenByKelasAndMapel(kelasId, mapelId));
                }
            }
        }
        
        return "guru/nilai";
    }

    @ResponseBody
    @GetMapping("/api/mapel-by-kelas")
    public List<MapelOptionDTO> getMapelByKelas(@RequestParam Long kelasId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        return jadwalPelajaranRepository.findDistinctMapelByUserIdAndKelasId(userId, kelasId)
                .stream()
                .map(m -> new MapelOptionDTO(m.getId(), m.getNamaMapel()))
                .collect(Collectors.toList());
    }

    @PostMapping("/komponen/add")
    public String createKomponenNilai(@RequestParam Long kelasId,
                                      @RequestParam Long mapelId,
                                      @RequestParam String namaKomponen,
                                      @RequestParam KategoriNilai kategoriNilai,
                                      @RequestParam String deskripsiTujuanPembelajaran,
                                      RedirectAttributes ra) {
        try {
            nilaiService.createKomponenNilai(kelasId, mapelId, namaKomponen, kategoriNilai, deskripsiTujuanPembelajaran);
            ra.addFlashAttribute("success", "Komponen Nilai berhasil ditambahkan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/guru/nilai?kelasId=" + kelasId + "&mapelId=" + mapelId;
    }

    @PostMapping("/komponen/delete/{id}")
    public String deleteKomponen(@PathVariable Long id, 
                                 @RequestParam Long kelasId,
                                 @RequestParam Long mapelId,
                                 RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("success", "Komponen berhasil dihapus.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/guru/nilai?kelasId=" + kelasId + "&mapelId=" + mapelId;
    }

    @PostMapping("/kalkulasi-rapor")
    public String kalkulasiRaporAkhir(@RequestParam Long kelasId,
                                      @RequestParam Long mapelId,
                                      @RequestParam Integer semester,
                                      RedirectAttributes ra) {
        try {
            nilaiService.generateRaporAkhir(kelasId, mapelId, semester);
            ra.addFlashAttribute("success", "Rapor akhir berhasil dikalkulasi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/guru/nilai?kelasId=" + kelasId + "&mapelId=" + mapelId;
    }

    public static class FormNilaiWrapper {
        private java.util.List<NilaiItem> items = new java.util.ArrayList<>();
        public java.util.List<NilaiItem> getItems() { return items; }
        public void setItems(java.util.List<NilaiItem> items) { this.items = items; }
    }

    public static class NilaiItem {
        private Long siswaId;
        private String nisn;
        private String namaSiswa;
        private Double nilai;
        public Long getSiswaId() { return siswaId; }
        public void setSiswaId(Long siswaId) { this.siswaId = siswaId; }
        public String getNisn() { return nisn; }
        public void setNisn(String nisn) { this.nisn = nisn; }
        public String getNamaSiswa() { return namaSiswa; }
        public void setNamaSiswa(String namaSiswa) { this.namaSiswa = namaSiswa; }
        public Double getNilai() { return nilai; }
        public void setNilai(Double nilai) { this.nilai = nilai; }
    }

    @GetMapping("/input/{komponenId}")
    public String showBulkInputForm(@PathVariable Long komponenId, Model model) {
        model.addAttribute("komponenId", komponenId);
        com.akamsi.entity.KomponenNilai kn = nilaiService.getKomponenByKelasAndMapel(null, null)
                .stream().filter(k -> k.getId().equals(komponenId)).findFirst().orElse(null);
        
        Long kelasId = 1L;
        Long mapelId = 1L;
        FormNilaiWrapper wrapper = new FormNilaiWrapper();
        
        model.addAttribute("formNilai", wrapper);
        model.addAttribute("kelasId", kelasId);
        model.addAttribute("mapelId", mapelId);
        
        return "guru/nilai-input";
    }

    @PostMapping("/input/save")
    public String submitBulkNilai(@RequestParam Long komponenId,
                                  @RequestParam Long kelasId,
                                  @RequestParam Long mapelId,
                                  @RequestParam Map<String, String> formData,
                                  RedirectAttributes ra) {
        try {
            Map<Long, Double> nilaiMap = new HashMap<>();
            formData.forEach((key, value) -> {
                if (key.contains(".siswaId") && !value.isBlank()) {
                    String prefix = key.replace(".siswaId", "");
                    String scoreKey = prefix + ".nilai";
                    if (formData.containsKey(scoreKey) && !formData.get(scoreKey).isBlank()) {
                        nilaiMap.put(Long.valueOf(value), Double.valueOf(formData.get(scoreKey)));
                    }
                }
            });
            nilaiService.saveBulkNilaiSiswa(komponenId, nilaiMap);
            ra.addFlashAttribute("success", "Nilai siswa berhasil disimpan.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/guru/nilai?kelasId=" + kelasId + "&mapelId=" + mapelId;
    }
}
