package com.akamsi.service;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SiswaPanelService {
    private final SiswaRepository siswaRepository;
    private final PresensiRepository presensiRepository;
    private final NilaiRepository nilaiRepository;
    private final PembayaranSPPRepository sppRepository;

    public SiswaPanelService(SiswaRepository siswaRepository,
                             PresensiRepository presensiRepository,
                             NilaiRepository nilaiRepository,
                             PembayaranSPPRepository sppRepository) {
        this.siswaRepository = siswaRepository;
        this.presensiRepository = presensiRepository;
        this.nilaiRepository = nilaiRepository;
        this.sppRepository = sppRepository;
    }

    public Siswa getSiswaProfile(User user) {
        return siswaRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Akun ini belum dikaitkan dengan data Siswa!"));
    }

    public List<Presensi> getAttendanceHistory(Siswa siswa) {
        return presensiRepository.findBySiswaIdOrderByTanggalDesc(siswa.getId());
    }

    public List<Nilai> getReportCard(Siswa siswa, String semester) {
        return nilaiRepository.findBySiswaIdAndSemester(siswa.getId(), semester);
    }

    public List<PembayaranSPP> getSppHistory(Siswa siswa) {
        return sppRepository.findBySiswaId(siswa.getId());
    }
}
