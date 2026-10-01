package com.akamsi.service;

import com.akamsi.dto.RaporEditDTO;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.entity.NilaiSiswa;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.MataPelajaranRepository;
import com.akamsi.repository.NilaiSiswaRepository;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.SiswaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GuruRaporService {

    private final RaporAkhirRepository raporAkhirRepository;
    private final SiswaRepository siswaRepository;
    private final NilaiSiswaRepository nilaiSiswaRepository;
    private final MataPelajaranRepository mapelRepository;

    public GuruRaporService(RaporAkhirRepository raporAkhirRepository,
                            SiswaRepository siswaRepository,
                            NilaiSiswaRepository nilaiSiswaRepository,
                            MataPelajaranRepository mapelRepository) {
        this.raporAkhirRepository = raporAkhirRepository;
        this.siswaRepository = siswaRepository;
        this.nilaiSiswaRepository = nilaiSiswaRepository;
        this.mapelRepository = mapelRepository;
    }

    @Transactional(readOnly = true)
    public List<RaporAkhir> getRaporBySiswa(Long siswaId, Integer semester) {
        return raporAkhirRepository.findBySiswaIdAndSemester(siswaId, semester);
    }

    public void kalkulasiRaporSiswa(Long siswaId, Integer semester) {
        Siswa siswa = siswaRepository.findById(siswaId)
                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));

        List<MataPelajaran> mapelList = mapelRepository.findAll();

        for (MataPelajaran mapel : mapelList) {
            List<NilaiSiswa> nilaiList = nilaiSiswaRepository.findBySiswaId(siswa.getId()).stream()
                    .filter(n -> n.getKomponenNilai() != null && n.getKomponenNilai().getMapel().getId().equals(mapel.getId()))
                    .collect(Collectors.toList());

            if (nilaiList.isEmpty()) {
                continue;
            }

            double sum = 0.0;
            for (NilaiSiswa ns : nilaiList) {
                sum += ns.getNilai();
            }
            double average = sum / nilaiList.size();
            
            String predikat;
            String deskripsi;
            if (average >= 85) {
                predikat = "A";
                deskripsi = "Sangat Baik. Menunjukkan penguasaan yang sangat baik dalam mencapai tujuan pembelajaran pada materi semester ini.";
            } else if (average >= 75) {
                predikat = "B";
                deskripsi = "Baik. Menunjukkan pemahaman yang baik terhadap materi yang diajarkan.";
            } else if (average >= 65) {
                predikat = "C";
                deskripsi = "Cukup. Perlu meningkatkan fokus untuk mencapai tujuan pembelajaran secara menyeluruh.";
            } else {
                predikat = "D";
                deskripsi = "Perlu Bimbingan. Diharapkan mengikuti program remedial untuk meningkatkan pemahaman dasar.";
            }

            Optional<RaporAkhir> existingOpt = raporAkhirRepository.findBySiswaIdAndMapelIdAndSemester(siswa.getId(), mapel.getId(), semester);
            RaporAkhir rapor = existingOpt.orElse(new RaporAkhir());
            
            rapor.setSiswa(siswa);
            rapor.setMapel(mapel);
            rapor.setSemester(semester);
            rapor.setNilaiAkhir(Math.round(average * 100.0) / 100.0);
            rapor.setPredikat(predikat);
            rapor.setDeskripsiCapaian(deskripsi);

            raporAkhirRepository.save(rapor);
        }
    }

    public void updateRapor(RaporEditDTO dto) {
        RaporAkhir rapor = raporAkhirRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Rapor tidak ditemukan"));
        rapor.setNilaiAkhir(dto.getNilaiAkhir());
        rapor.setPredikat(dto.getPredikat());
        rapor.setDeskripsiCapaian(dto.getDeskripsiCapaian());
        raporAkhirRepository.save(rapor);
    }

    public void resetRaporSiswa(Long siswaId, Integer semester) {
        raporAkhirRepository.deleteBySiswaIdAndSemester(siswaId, semester);
    }
}
