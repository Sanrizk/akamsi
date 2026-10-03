package com.akamsi.service;

import com.akamsi.dto.PresensiMingguanSummaryDTO;
import com.akamsi.dto.SiswaPresensiFilterDTO;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.Pengumuman;
import com.akamsi.entity.Presensi;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.StatusPresensi;
import com.akamsi.repository.PembayaranSPPRepository;
import com.akamsi.repository.PengumumanRepository;
import com.akamsi.repository.PresensiRepository;
import com.akamsi.repository.SiswaRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SiswaDashboardService {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PembayaranSPPRepository pembayaranSPPRepository;

    @Autowired
    private PresensiRepository presensiRepository;

    @Autowired
    private PengumumanRepository pengumumanRepository;

    private static final List<String> DAFTAR_BULAN = Arrays.asList(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    );

    public String getBulanIndo(int monthNumber) {
        if (monthNumber >= 1 && monthNumber <= 12) {
            return DAFTAR_BULAN.get(monthNumber - 1);
        }
        return "Januari";
    }

    @Transactional(readOnly = true)
    public Optional<Siswa> getSiswaByUserId(Long userId) {
        return siswaRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<PembayaranSPP> getSppBulanAktif(Long siswaId) {
        LocalDate now = LocalDate.now();
        String currentBulan = getBulanIndo(now.getMonthValue());
        int currentTahun = now.getYear();
        return pembayaranSPPRepository.findBySiswaIdAndBulanAndTahun(siswaId, currentBulan, currentTahun);
    }

    @Transactional(readOnly = true)
    public PresensiMingguanSummaryDTO getKehadiranMingguan(Long siswaId) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Presensi> presensiList = presensiRepository.findBySiswaIdAndTanggalBetween(siswaId, monday, sunday);

        int h = 0;
        int s = 0;
        int iz = 0;
        int a = 0;

        for (Presensi p : presensiList) {
            if (p.getStatus() == StatusPresensi.HADIR) h++;
            else if (p.getStatus() == StatusPresensi.SAKIT) s++;
            else if (p.getStatus() == StatusPresensi.IZIN) iz++;
            else if (p.getStatus() == StatusPresensi.ALPA) a++;
        }

        int total = h + s + iz + a;
        double persentase = (total > 0) ? Math.round(((double) h / total * 100.0) * 10.0) / 10.0 : 100.0;

        return new PresensiMingguanSummaryDTO(h, s, iz, a, total, persentase);
    }

    @Transactional(readOnly = true)
    public List<Pengumuman> getPengumumanSiswa() {
        return pengumumanRepository.findForSiswa();
    }

    @Transactional(readOnly = true)
    public List<Presensi> getRiwayatPresensiSiswa(Long siswaId, SiswaPresensiFilterDTO filter) {
        if (filter.getMapelId() == null) {
            return presensiRepository.findBySiswaIdAndTanggalBetween(
                    siswaId, filter.getStartDate(), filter.getEndDate());
        } else {
            return presensiRepository.findBySiswaIdAndMapelIdAndTanggalBetween(
                    siswaId, filter.getMapelId(), filter.getStartDate(), filter.getEndDate());
        }
    }

    public PresensiMingguanSummaryDTO calculatePresensiSummary(List<Presensi> list) {
        int h = 0;
        int s = 0;
        int iz = 0;
        int a = 0;

        for (Presensi p : list) {
            if (p.getStatus() == StatusPresensi.HADIR) h++;
            else if (p.getStatus() == StatusPresensi.SAKIT) s++;
            else if (p.getStatus() == StatusPresensi.IZIN) iz++;
            else if (p.getStatus() == StatusPresensi.ALPA) a++;
        }

        int total = h + s + iz + a;
        double persentase = (total > 0) ? Math.round(((double) h / total * 100.0) * 10.0) / 10.0 : 100.0;

        return new PresensiMingguanSummaryDTO(h, s, iz, a, total, persentase);
    }
}
