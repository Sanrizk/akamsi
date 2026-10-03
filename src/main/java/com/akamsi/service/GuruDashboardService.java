package com.akamsi.service;

import com.akamsi.dto.KelasAjarSummaryDTO;
import com.akamsi.entity.Guru;
import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.Pengumuman;
import com.akamsi.repository.GuruRepository;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.repository.PengumumanRepository;
import com.akamsi.repository.SiswaRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuruDashboardService {

    @Autowired
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PengumumanRepository pengumumanRepository;

    public String getHariIniIndo() {
        DayOfWeek dow = LocalDate.now().getDayOfWeek();
        switch (dow) {
            case MONDAY: return "Senin";
            case TUESDAY: return "Selasa";
            case WEDNESDAY: return "Rabu";
            case THURSDAY: return "Kamis";
            case FRIDAY: return "Jumat";
            case SATURDAY: return "Sabtu";
            default: return "Minggu";
        }
    }

    @Transactional(readOnly = true)
    public Optional<Guru> getGuruProfile(Long userId) {
        return guruRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<JadwalPelajaran> getJadwalHariIni(Long userId) {
        String hariIni = getHariIniIndo();
        return jadwalPelajaranRepository.findByGuruUserIdAndHari(userId, hariIni);
    }

    @Transactional(readOnly = true)
    public List<JadwalPelajaran> getAllJadwalMingguan(Long userId) {
        return jadwalPelajaranRepository.findByGuruUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<KelasAjarSummaryDTO> getKelasAjarSummary(Long userId) {
        List<Kelas> distinctKelas = jadwalPelajaranRepository.findDistinctKelasByUserId(userId);
        List<JadwalPelajaran> allJadwal = jadwalPelajaranRepository.findByGuruUserId(userId);
        List<KelasAjarSummaryDTO> result = new ArrayList<>();

        for (Kelas k : distinctKelas) {
            int totalSiswa = siswaRepository.findByKelasId(k.getId()).size();

            String mapels = allJadwal.stream()
                    .filter(j -> j.getKelas().getId().equals(k.getId()))
                    .map(j -> j.getMapel().getNamaMapel())
                    .distinct()
                    .collect(Collectors.joining(", "));

            result.add(new KelasAjarSummaryDTO(k.getId(), k.getNamaKelas(), totalSiswa, mapels));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<Pengumuman> getPengumumanGuru() {
        return pengumumanRepository.findForGuru();
    }
}

