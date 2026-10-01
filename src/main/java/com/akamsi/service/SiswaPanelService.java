package com.akamsi.service;

import com.akamsi.entity.Presensi;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.User;
import com.akamsi.repository.PresensiRepository;
import com.akamsi.repository.PembayaranSPPRepository;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.SiswaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiswaPanelService {
    private final SiswaRepository siswaRepository;
    private final PresensiRepository presensiRepository;
    private final PembayaranSPPRepository sppRepository;
    private final RaporAkhirRepository raporRepo;

    public SiswaPanelService(SiswaRepository siswaRepository, PresensiRepository presensiRepository,
                             PembayaranSPPRepository sppRepository, RaporAkhirRepository raporRepo) {
        this.siswaRepository = siswaRepository;
        this.presensiRepository = presensiRepository;
        this.sppRepository = sppRepository;
        this.raporRepo = raporRepo;
    }

    public Siswa getSiswaProfile(User user) {
        return siswaRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Profil siswa tidak ditemukan."));
    }

    public List<Presensi> getAttendanceHistory(Siswa siswa) {
        return presensiRepository.findBySiswaIdOrderByTanggalDesc(siswa.getId());
    }

    public List<RaporAkhir> getReportCard(Siswa siswa, String semester) {
        return raporRepo.findBySiswaIdAndSemester(siswa.getId(), semester);
    }

    public List<PembayaranSPP> getSppHistory(Siswa siswa) {
        return sppRepository.findBySiswaId(siswa.getId());
    }
}
