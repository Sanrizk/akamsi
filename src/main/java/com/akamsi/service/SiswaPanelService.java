package com.akamsi.service;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SiswaPanelService {
    private final SiswaRepository siswaRepository;
    private final PresensiRepository presensiRepository;
    private final PembayaranSPPRepository sppRepository;
    private final RaporAkhirRepository raporRepo;
    private final NilaiSiswaRepository nilaiSiswaRepo;

    public SiswaPanelService(SiswaRepository siswaRepository, PresensiRepository presensiRepository,
                             PembayaranSPPRepository sppRepository, RaporAkhirRepository raporRepo,
                             NilaiSiswaRepository nilaiSiswaRepo) {
        this.siswaRepository = siswaRepository;
        this.presensiRepository = presensiRepository;
        this.sppRepository = sppRepository;
        this.raporRepo = raporRepo;
        this.nilaiSiswaRepo = nilaiSiswaRepo;
    }

    public Siswa getSiswaProfile(User user) {
        return siswaRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Profil siswa tidak ditemukan."));
    }

    public List<Presensi> getAttendanceHistory(Siswa siswa) {
        return presensiRepository.findBySiswaIdOrderByTanggalDesc(siswa.getId());
    }

    public List<PembayaranSPP> getSppHistory(Siswa siswa) {
        return sppRepository.findBySiswaId(siswa.getId());
    }

    // Existing backwards compatibility / simpler method
    public List<RaporAkhir> getReportCard(Siswa siswa, Integer semester) {
        return raporRepo.findBySiswaIdAndSemester(siswa.getId(), semester);
    }

    /**
     * Returns a comprehensive map containing both the final RaporAkhir 
     * and the detailed breakdowns (NilaiSiswa) for the student view.
     */
    public Map<String, Object> getRaporSiswa(Long siswaId, Integer semester) {
        List<RaporAkhir> raporAkhirList = raporRepo.findBySiswaIdAndSemester(siswaId, semester);
        
        // Optionally, fetch the detailed component scores
        List<NilaiSiswa> detailNilai = nilaiSiswaRepo.findBySiswaId(siswaId);

        Map<String, Object> result = new HashMap<>();
        result.put("raporAkhir", raporAkhirList);
        result.put("detailNilai", detailNilai);
        
        return result;
    }
}
