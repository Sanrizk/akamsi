package com.akamsi.service;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class GuruPresensiService {
    private final PresensiRepository presensiRepository;
    private final KelasRepository kelasRepository;
    private final GuruRepository guruRepository;
    private final SiswaRepository siswaRepository;

    public GuruPresensiService(PresensiRepository presensiRepository, 
                               KelasRepository kelasRepository,
                               GuruRepository guruRepository,
                               SiswaRepository siswaRepository) {
        this.presensiRepository = presensiRepository;
        this.kelasRepository = kelasRepository;
        this.guruRepository = guruRepository;
        this.siswaRepository = siswaRepository;
    }

    public List<Siswa> getSiswaByGuru(User user) {
        Guru guru = guruRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Akun ini belum dikaitkan dengan data Guru"));
        
        Kelas kelas = kelasRepository.findByWaliKelasId(guru.getId())
                .orElseThrow(() -> new IllegalArgumentException("Guru ini belum menjadi Wali Kelas"));
                
        return siswaRepository.findByKelasId(kelas.getId());
    }

    public void submitPresensi(Long siswaId, LocalDate tanggal, StatusPresensi status, String keterangan) {
        Siswa siswa = siswaRepository.findById(siswaId).orElseThrow();
        Presensi presensi = new Presensi();
        presensi.setSiswa(siswa);
        presensi.setTanggal(tanggal);
        presensi.setStatus(status);
        presensi.setKeterangan(keterangan);
        presensiRepository.save(presensi);
    }
    
    public List<Presensi> getPresensiSummary(Long siswaId, int month, int year) {
        LocalDate start = YearMonth.of(year, month).atDay(1);
        LocalDate end = YearMonth.of(year, month).atEndOfMonth();
        return presensiRepository.findBySiswaIdAndTanggalBetween(siswaId, start, end);
    }
}
