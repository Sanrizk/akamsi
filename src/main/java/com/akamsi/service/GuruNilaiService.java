package com.akamsi.service;

import com.akamsi.entity.KomponenNilai;
import com.akamsi.entity.NilaiSiswa;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.KomponenNilaiRepository;
import com.akamsi.repository.NilaiSiswaRepository;
import com.akamsi.repository.RaporAkhirRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuruNilaiService {
    
    private final KomponenNilaiRepository komponenRepo;
    private final NilaiSiswaRepository nilaiSiswaRepo;
    private final RaporAkhirRepository raporRepo;

    public GuruNilaiService(KomponenNilaiRepository komponenRepo, 
                            NilaiSiswaRepository nilaiSiswaRepo, 
                            RaporAkhirRepository raporRepo) {
        this.komponenRepo = komponenRepo;
        this.nilaiSiswaRepo = nilaiSiswaRepo;
        this.raporRepo = raporRepo;
    }

    public List<RaporAkhir> getReportCard(Long siswaId, String semester) {
        return raporRepo.findBySiswaIdAndSemester(siswaId, semester);
    }
    
    // Stub to prevent compilation error from the old GuruNilaiController
    public void saveOrUpdateNilai(Long siswaId, Long mapelId, String semester, Double tugas, Double uts, Double uas) {
        // TODO: Refactor GuruNilaiController to accept List<NilaiSiswa> instead of static fields
    }
}
