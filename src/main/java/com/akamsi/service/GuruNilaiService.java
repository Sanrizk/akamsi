package com.akamsi.service;

import com.akamsi.dto.FormInputNilaiDTO;
import com.akamsi.dto.ItemNilaiDTO;
import com.akamsi.entity.KomponenNilai;
import com.akamsi.entity.NilaiSiswa;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.KategoriNilai;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.KomponenNilaiRepository;
import com.akamsi.repository.NilaiSiswaRepository;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.MataPelajaranRepository;
import com.akamsi.repository.SiswaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuruNilaiService {
    
    @Autowired
    private KomponenNilaiRepository komponenNilaiRepository;

    @Autowired
    private NilaiSiswaRepository nilaiSiswaRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private RaporAkhirRepository raporRepo;

    @Autowired
    private KelasRepository kelasRepo;

    @Autowired
    private MataPelajaranRepository mapelRepo;

    public List<KomponenNilai> getKomponenByKelasAndMapel(Long kelasId, Long mapelId) {
        return komponenNilaiRepository.findByKelasIdAndMapelId(kelasId, mapelId);
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
        
        komponenNilaiRepository.save(kn);
    }

    @Transactional(readOnly = true)
    public KomponenNilai getKomponenById(Long id) {
        KomponenNilai kn = komponenNilaiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Komponen tidak ditemukan"));
        // Force initialize LAZY relations
        if (kn.getKelas() != null) kn.getKelas().getNamaKelas();
        if (kn.getMapel() != null) kn.getMapel().getNamaMapel();
        return kn;
    }

    @Transactional(readOnly = true)
    public FormInputNilaiDTO getFormInputNilai(Long komponenId) {
        KomponenNilai komponen = komponenNilaiRepository.findById(komponenId)
                .orElseThrow(() -> new RuntimeException("Komponen nilai tidak ditemukan dengan ID: " + komponenId));

        if (komponen.getKelas() == null) {
            throw new IllegalStateException("Komponen penilaian tidak terhubung dengan Kelas mana pun.");
        }

        Long kelasId = komponen.getKelas().getId();
        List<Siswa> siswaList = siswaRepository.findByKelasId(kelasId);

        List<NilaiSiswa> existingGrades = nilaiSiswaRepository.findByKomponenNilaiId(komponenId);
        Map<Long, Double> gradeMap = existingGrades.stream()
                .collect(Collectors.toMap(
                        n -> n.getSiswa().getId(),
                        NilaiSiswa::getNilai,
                        (existing, replacing) -> existing
                ));

        FormInputNilaiDTO form = new FormInputNilaiDTO();
        form.setKomponenId(komponen.getId());
        form.setKelasId(kelasId);
        form.setMapelId(komponen.getMapel().getId());

        List<ItemNilaiDTO> items = new ArrayList<>();
        for (Siswa s : siswaList) {
            ItemNilaiDTO item = new ItemNilaiDTO();
            item.setSiswaId(s.getId());
            item.setNisn(s.getNisn());
            item.setNamaSiswa(s.getNamaSiswa());
            item.setNilai(gradeMap.get(s.getId()));
            items.add(item);
        }
        form.setItems(items);

        return form;
    }

    @Transactional
    public void saveBulkNilai(FormInputNilaiDTO form) {
        KomponenNilai komponen = komponenNilaiRepository.findById(form.getKomponenId())
                .orElseThrow(() -> new RuntimeException("Komponen tidak ditemukan"));

        if (form.getItems() != null) {
            for (ItemNilaiDTO item : form.getItems()) {
                if (item.getNilai() != null) {
                    Optional<NilaiSiswa> opt = nilaiSiswaRepository
                            .findByKomponenNilaiIdAndSiswaId(komponen.getId(), item.getSiswaId());

                    NilaiSiswa ns = opt.orElseGet(NilaiSiswa::new);
                    if (ns.getId() == null) {
                        Siswa s = siswaRepository.findById(item.getSiswaId())
                                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));
                        ns.setSiswa(s);
                        ns.setKomponenNilai(komponen);
                    }
                    ns.setNilai(item.getNilai());
                    nilaiSiswaRepository.save(ns);
                }
            }
        }
    }

    public void generateRaporAkhir(Long kelasId, Long mapelId, Integer semester) {
        MataPelajaran mapel = mapelRepo.findById(mapelId)
                .orElseThrow(() -> new IllegalArgumentException("Mapel not found"));

        List<KomponenNilai> komponenList = komponenNilaiRepository.findByKelasIdAndMapelId(kelasId, mapelId);
        List<Siswa> siswaList = siswaRepository.findAll().stream()
                .filter(s -> s.getKelas() != null && s.getKelas().getId().equals(kelasId))
                .toList();

        for (Siswa s : siswaList) {
            List<NilaiSiswa> nilaiListSiswa = nilaiSiswaRepository.findBySiswaId(s.getId()).stream()
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
