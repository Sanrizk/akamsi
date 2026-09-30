package com.akamsi.service;

import com.akamsi.entity.MataPelajaran;
import com.akamsi.entity.Nilai;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.MataPelajaranRepository;
import com.akamsi.repository.NilaiRepository;
import com.akamsi.repository.SiswaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuruNilaiService {
    private final NilaiRepository nilaiRepository;
    private final SiswaRepository siswaRepository;
    private final MataPelajaranRepository mapelRepository;

    public GuruNilaiService(NilaiRepository nilaiRepository, SiswaRepository siswaRepository, MataPelajaranRepository mapelRepository) {
        this.nilaiRepository = nilaiRepository;
        this.siswaRepository = siswaRepository;
        this.mapelRepository = mapelRepository;
    }

    public void saveOrUpdateNilai(Long siswaId, Long mapelId, String semester, Double tugas, Double uts, Double uas) {
        Siswa siswa = siswaRepository.findById(siswaId).orElseThrow();
        MataPelajaran mapel = mapelRepository.findById(mapelId).orElseThrow();

        Nilai nilai = nilaiRepository.findBySiswaIdAndMapelIdAndSemester(siswaId, mapelId, semester)
                .orElse(new Nilai());
        
        nilai.setSiswa(siswa);
        nilai.setMapel(mapel);
        nilai.setSemester(semester);
        nilai.setNilaiTugas(tugas);
        nilai.setNilaiUts(uts);
        nilai.setNilaiUas(uas);
        
        // Final Grade Calculation Logic: 30% Tugas + 30% UTS + 40% UAS
        double akhir = (tugas * 0.3) + (uts * 0.3) + (uas * 0.4);
        nilai.setNilaiAkhir(akhir);
        
        nilaiRepository.save(nilai);
    }
    
    public List<Nilai> getReportCard(Long siswaId, String semester) {
        return nilaiRepository.findBySiswaIdAndSemester(siswaId, semester);
    }
}
