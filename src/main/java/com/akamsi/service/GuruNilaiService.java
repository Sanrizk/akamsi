package com.akamsi.service;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GuruNilaiService {
    
    private final KomponenNilaiRepository komponenRepo;
    private final NilaiSiswaRepository nilaiSiswaRepo;
    private final RaporAkhirRepository raporRepo;
    private final KelasRepository kelasRepo;
    private final MataPelajaranRepository mapelRepo;
    private final SiswaRepository siswaRepo;

    public GuruNilaiService(KomponenNilaiRepository komponenRepo, 
                            NilaiSiswaRepository nilaiSiswaRepo, 
                            RaporAkhirRepository raporRepo,
                            KelasRepository kelasRepo,
                            MataPelajaranRepository mapelRepo,
                            SiswaRepository siswaRepo) {
        this.komponenRepo = komponenRepo;
        this.nilaiSiswaRepo = nilaiSiswaRepo;
        this.raporRepo = raporRepo;
        this.kelasRepo = kelasRepo;
        this.mapelRepo = mapelRepo;
        this.siswaRepo = siswaRepo;
    }

    public List<KomponenNilai> getKomponenByKelasAndMapel(Long kelasId, Long mapelId) {
        return komponenRepo.findByKelasIdAndMapelId(kelasId, mapelId);
    }

    public void createKomponenNilai(Long kelasId, Long mapelId, String namaKomponen, 
                                    KategoriNilai kategoriNilai, String deskripsiTujuan) {
        Kelas kelas = kelasRepo.findById(kelasId)
                .orElseThrow(() -> new IllegalArgumentException("Kelas not found"));
        MataPelajaran mapel = mapelRepo.findById(mapelId)
                .orElseThrow(() -> new IllegalArgumentException("Mapel not found"));

        KomponenNilai kn = new KomponenNilai();
        kn.setKelas(kelas);
        kn.setMapel(mapel);
        kn.setNamaKomponen(namaKomponen);
        kn.setKategoriNilai(kategoriNilai);
        kn.setDeskripsiTujuanPembelajaran(deskripsiTujuan);
        
        komponenRepo.save(kn);
    }

    public void saveBulkNilaiSiswa(Long komponenId, Map<Long, Double> nilaiMap) {
        KomponenNilai kn = komponenRepo.findById(komponenId)
                .orElseThrow(() -> new IllegalArgumentException("Komponen not found"));

        for (Map.Entry<Long, Double> entry : nilaiMap.entrySet()) {
            Long siswaId = entry.getKey();
            Double score = entry.getValue();

            Siswa siswa = siswaRepo.findById(siswaId)
                    .orElseThrow(() -> new IllegalArgumentException("Siswa not found for id " + siswaId));
            
            // Note: Ideally you would check if NilaiSiswa already exists and update it, 
            // but for simplicity in this bulk logic we are creating new ones or assuming clean inserts.
            // (In a real app, query by siswaId and komponenId first).
            NilaiSiswa ns = new NilaiSiswa();
            ns.setSiswa(siswa);
            ns.setKomponenNilai(kn);
            ns.setNilai(score);
            
            nilaiSiswaRepo.save(ns);
        }
    }

    public void generateRaporAkhir(Long kelasId, Long mapelId, Integer semester) {
        MataPelajaran mapel = mapelRepo.findById(mapelId)
                .orElseThrow(() -> new IllegalArgumentException("Mapel not found"));

        // Fetch all components for this subject and class
        List<KomponenNilai> komponenList = komponenRepo.findByKelasIdAndMapelId(kelasId, mapelId);
        List<Siswa> siswaList = siswaRepo.findAll().stream()
                .filter(s -> s.getKelas() != null && s.getKelas().getId().equals(kelasId))
                .toList();

        for (Siswa s : siswaList) {
            List<NilaiSiswa> nilaiListSiswa = nilaiSiswaRepo.findBySiswaId(s.getId()).stream()
                    .filter(n -> n.getKomponenNilai().getMapel().getId().equals(mapelId))
                    .toList();

            if (nilaiListSiswa.isEmpty()) {
                continue;
            }

            double sum = 0.0;
            for (NilaiSiswa ns : nilaiListSiswa) {
                sum += ns.getNilai();
            }
            double average = sum / nilaiListSiswa.size();

            String deskripsi;
            if (average >= 85) {
                deskripsi = "Sangat baik dalam menguasai materi.";
            } else if (average >= 70) {
                deskripsi = "Cukup baik dalam memahami materi.";
            } else {
                deskripsi = "Perlu bimbingan lebih lanjut.";
            }

            RaporAkhir rapor = new RaporAkhir();
            rapor.setSiswa(s);
            rapor.setMapel(mapel);
            rapor.setSemester(semester);
            rapor.setNilaiAkhir(average);
            rapor.setDeskripsiCapaian(deskripsi);

            raporRepo.save(rapor);
        }
    }

    public List<RaporAkhir> getReportCard(Long siswaId, Integer semester) {
        return raporRepo.findBySiswaIdAndSemester(siswaId, semester);
    }
}
