package com.akamsi.controller;

import com.akamsi.dto.ProgresNilaiKelasDTO;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.service.AdminRaporService;
import com.akamsi.service.AkademikService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/rapor")
public class AdminRaporController {

    @Autowired
    private AdminRaporService adminRaporService;

    @Autowired
    private AkademikService akademikService;

    @Autowired
    private SiswaRepository siswaRepository;

    @GetMapping
    public String index(
            @RequestParam(defaultValue = "progres") String tab,
            @RequestParam(required = false) Long kelasId,
            @RequestParam(required = false) Long siswaId,
            @RequestParam(defaultValue = "1") Integer semester,
            Model model) {

        List<Kelas> kelasList = akademikService.getAllKelas();
        model.addAttribute("kelasList", kelasList);
        model.addAttribute("activeTab", tab);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("selectedKelasId", kelasId);

        // Tab 1: Progres Pengisian Nilai
        if ("progres".equals(tab)) {
            Long targetKelasId = (kelasId != null) ? kelasId : (!kelasList.isEmpty() ? kelasList.get(0).getId() : null);
            model.addAttribute("targetKelasId", targetKelasId);

            if (targetKelasId != null) {
                Kelas targetKelas = akademikService.findKelasById(targetKelasId);
                model.addAttribute("targetKelas", targetKelas);
                List<ProgresNilaiKelasDTO> progresList = adminRaporService.getProgresNilaiByKelas(targetKelasId, semester);
                model.addAttribute("progresList", progresList);
            }
        }

        // Tab 2: Cetak Rapor Siswa
        if ("cetak".equals(tab)) {
            if (kelasId != null) {
                model.addAttribute("siswaList", siswaRepository.findByKelasId(kelasId));
            }

            if (siswaId != null) {
                Siswa selectedSiswa = siswaRepository.findById(siswaId).orElse(null);
                model.addAttribute("selectedSiswa", selectedSiswa);

                if (selectedSiswa != null) {
                    Kelas kelas = selectedSiswa.getKelas();
                    String namaKelas = (kelas != null) ? kelas.getNamaKelas() : "";
                    model.addAttribute("namaKelas", namaKelas);

                    List<RaporAkhir> raporList = adminRaporService.getRaporSiswa(siswaId, namaKelas, semester);
                    model.addAttribute("raporList", raporList);

                    double rataRata = 0.0;
                    if (!raporList.isEmpty()) {
                        double total = 0.0;
                        for (RaporAkhir r : raporList) {
                            total += (r.getNilaiAkhir() != null ? r.getNilaiAkhir() : 0.0);
                        }
                        rataRata = Math.round((total / raporList.size()) * 10.0) / 10.0;
                    }
                    model.addAttribute("rataRata", rataRata);
                }
            }
        }

        return "admin/rapor";
    }
}
