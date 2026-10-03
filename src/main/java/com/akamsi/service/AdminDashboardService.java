package com.akamsi.service;

import com.akamsi.dto.PresensiHariIniDTO;
import com.akamsi.entity.StatusPresensi;
import com.akamsi.repository.GuruRepository;
import com.akamsi.repository.PresensiRepository;
import com.akamsi.repository.SiswaRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminDashboardService {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private PresensiRepository presensiRepository;

    @Transactional(readOnly = true)
    public long getTotalSiswa() {
        return siswaRepository.count();
    }

    @Transactional(readOnly = true)
    public long getTotalGuru() {
        return guruRepository.count();
    }

    @Transactional(readOnly = true)
    public PresensiHariIniDTO getRekapPresensiHariIni() {
        LocalDate today = LocalDate.now();

        long total = presensiRepository.countByTanggal(today);
        long hadir = presensiRepository.countByTanggalAndStatus(today, StatusPresensi.HADIR);
        long sakit = presensiRepository.countByTanggalAndStatus(today, StatusPresensi.SAKIT);
        long izin = presensiRepository.countByTanggalAndStatus(today, StatusPresensi.IZIN);
        long alpa = presensiRepository.countByTanggalAndStatus(today, StatusPresensi.ALPA);

        double persentase = (total > 0) ? Math.round(((double) hadir / total * 100.0) * 10.0) / 10.0 : 0.0;

        return new PresensiHariIniDTO(total, hadir, sakit, izin, alpa, persentase);
    }
}
