package com.akamsi.controller;

import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.security.CustomUserDetails;
import com.akamsi.service.GuruRaporService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/siswa/rapor")
public class SiswaRaporController {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private RaporAkhirRepository raporAkhirRepository;

    @Autowired
    private GuruRaporService guruRaporService;

    @GetMapping
    @Transactional(readOnly = true)
    public String showSiswaRapor(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String namaKelas,
            @RequestParam(defaultValue = "1") Integer semester,
            Model model) {

        Long userId = userDetails.getUser().getId();
        Siswa siswa = siswaRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Data siswa tidak ditemukan untuk akun ini."));

        // Fetch distinct snapshot class names from report history
        List<String> historicalClassNames = raporAkhirRepository.findDistinctNamaKelasBySiswaId(siswa.getId());
        Set<String> classSet = new LinkedHashSet<>(historicalClassNames);

        // Include current classroom name if available
        if (siswa.getKelas() != null) {
            classSet.add(siswa.getKelas().getNamaKelas());
        }

        List<String> availableClassNames = new ArrayList<>(classSet);

        // Determine selected class name
        String selectedNamaKelas = (namaKelas != null && !namaKelas.trim().isEmpty())
                ? namaKelas
                : (siswa.getKelas() != null ? siswa.getKelas().getNamaKelas() : (!availableClassNames.isEmpty() ? availableClassNames.get(0) : ""));

        List<RaporAkhir> raporList = new ArrayList<>();
        double rataRataKeseluruhan = 0.0;

        if (selectedNamaKelas != null && !selectedNamaKelas.isEmpty()) {
            raporList = guruRaporService.getRaporBySiswaAndNamaKelas(siswa.getId(), selectedNamaKelas, semester);

            if (!raporList.isEmpty()) {
                double total = 0.0;
                for (RaporAkhir r : raporList) {
                    total += (r.getNilaiAkhir() != null ? r.getNilaiAkhir() : 0.0);
                }
                rataRataKeseluruhan = Math.round((total / raporList.size()) * 10.0) / 10.0;
            }
        }

        model.addAttribute("siswa", siswa);
        model.addAttribute("availableClassNames", availableClassNames);
        model.addAttribute("selectedNamaKelas", selectedNamaKelas);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("raporList", raporList);
        model.addAttribute("rataRata", rataRataKeseluruhan);

        return "siswa/rapor";
    }
}
