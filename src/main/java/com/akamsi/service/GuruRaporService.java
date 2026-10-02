package com.akamsi.service;

import com.akamsi.dto.RaporEditDTO;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.KomponenNilai;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.entity.NilaiSiswa;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.service.AkademikService;
import com.akamsi.repository.KomponenNilaiRepository;
import com.akamsi.repository.NilaiSiswaRepository;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.SiswaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuruRaporService {

    @Autowired
    private RaporAkhirRepository raporAkhirRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private KomponenNilaiRepository komponenNilaiRepository;

    @Autowired
    private NilaiSiswaRepository nilaiSiswaRepository;

    @Autowired
    private AkademikService akademikService;

    @Transactional(readOnly = true)
    public List<RaporAkhir> getRaporBySiswaAndNamaKelas(Long siswaId, String namaKelas, Integer semester) {
        return raporAkhirRepository.findBySiswaIdAndNamaKelasAndSemester(siswaId, namaKelas, semester);
    }

    @Transactional
    public void kalkulasiRaporSiswa(Long siswaId, Long kelasId, Integer semester) {
        Siswa siswa = siswaRepository.findById(siswaId)
                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));

        Kelas kelas = (kelasId != null) 
                ? akademikService.findKelasById(kelasId) 
                : siswa.getKelas();

        String snapshotNamaKelas = kelas.getNamaKelas();
        List<MataPelajaran> mapelList = akademikService.getAllMapel();

        for (MataPelajaran mapel : mapelList) {
            List<KomponenNilai> komponenList = komponenNilaiRepository
                    .findByKelasIdAndMapelId(kelas.getId(), mapel.getId());

            if (komponenList.isEmpty()) {
                continue;
            }

            double totalNilai = 0.0;
            int countNilai = 0;

            for (KomponenNilai komp : komponenList) {
                Optional<NilaiSiswa> ns = nilaiSiswaRepository
                        .findByKomponenNilaiIdAndSiswaId(komp.getId(), siswaId);
                if (ns.isPresent() && ns.get().getNilai() != null) {
                    totalNilai += ns.get().getNilai();
                    countNilai++;
                }
            }

            if (countNilai > 0) {
                double rataRata = Math.round((totalNilai / countNilai) * 10.0) / 10.0;
                String predikat;
                String deskripsi;

                if (rataRata >= 85.0) {
                    predikat = "A";
                    deskripsi = "Menunjukkan pemahaman yang sangat mendalam dan melampaui tujuan pembelajaran utama.";
                } else if (rataRata >= 75.0) {
                    predikat = "B";
                    deskripsi = "Menunjukkan penguasaan materi yang baik dan tuntas dalam mencapai tujuan pembelajaran.";
                } else if (rataRata >= 65.0) {
                    predikat = "C";
                    deskripsi = "Mencapai ketuntasan minimal tujuan pembelajaran, perlu penguatan pada topik analisis.";
                } else {
                    predikat = "D";
                    deskripsi = "Perlu bimbingan dan pendampingan intensif dalam menguasai kompetensi dasar.";
                }

                RaporAkhir rapor = raporAkhirRepository
                        .findBySiswaIdAndNamaKelasAndMapelIdAndSemester(siswaId, snapshotNamaKelas, mapel.getId(), semester)
                        .orElseGet(RaporAkhir::new);

                rapor.setSiswa(siswa);
                rapor.setNamaKelas(snapshotNamaKelas); // Save snapshot value
                rapor.setMapel(mapel);
                rapor.setSemester(semester);
                rapor.setNilaiAkhir(rataRata);
                rapor.setPredikat(predikat);
                rapor.setDeskripsiCapaian(deskripsi);

                raporAkhirRepository.save(rapor);
            }
        }
    }

    @Transactional
    public void updateRapor(RaporEditDTO dto) {
        RaporAkhir rapor = raporAkhirRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Rapor tidak ditemukan"));

        rapor.setNilaiAkhir(dto.getNilaiAkhir());
        rapor.setPredikat(dto.getPredikat());
        rapor.setDeskripsiCapaian(dto.getDeskripsiCapaian());
        raporAkhirRepository.save(rapor);
    }

    @Transactional
    public void resetRaporSiswa(Long siswaId, String namaKelas, Integer semester) {
        raporAkhirRepository.deleteBySiswaIdAndNamaKelasAndSemester(siswaId, namaKelas, semester);
    }

    // --- Backwards Compatibility Wrappers ---
    @Transactional(readOnly = true)
    public List<RaporAkhir> getRaporBySiswa(Long siswaId, Integer semester) {
        return raporAkhirRepository.findBySiswaIdAndSemester(siswaId, semester);
    }

    @Transactional
    public void kalkulasiRaporSiswa(Long siswaId, Integer semester) {
        kalkulasiRaporSiswa(siswaId, null, semester);
    }

    @Transactional
    public void resetRaporSiswa(Long siswaId, Integer semester) {
        Siswa siswa = siswaRepository.findById(siswaId).orElse(null);
        if (siswa != null && siswa.getKelas() != null) {
            resetRaporSiswa(siswaId, siswa.getKelas().getNamaKelas(), semester);
        }
    }
}
