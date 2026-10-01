package com.akamsi.service;

import com.akamsi.entity.Kelas;
import com.akamsi.entity.Kurikulum;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.KurikulumRepository;
import com.akamsi.repository.MataPelajaranRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AkademikService {
    private final KelasRepository kelasRepository;
    private final MataPelajaranRepository mapelRepository;
    private final KurikulumRepository kurikulumRepository;

    public AkademikService(KelasRepository kelasRepository, 
                           MataPelajaranRepository mapelRepository,
                           KurikulumRepository kurikulumRepository) {
        this.kelasRepository = kelasRepository;
        this.mapelRepository = mapelRepository;
        this.kurikulumRepository = kurikulumRepository;
    }

    // --- KURIKULUM ---
    public List<Kurikulum> getAllKurikulum() {
        return kurikulumRepository.findAll();
    }

    public Kurikulum getKurikulumById(Long id) {
        return kurikulumRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Kurikulum tidak ditemukan"));
    }

    public void saveKurikulum(Kurikulum kurikulum) {
        kurikulumRepository.save(kurikulum);
    }

    public void updateKurikulum(Long id, Kurikulum updatedKurikulum) {
        Kurikulum kurikulum = getKurikulumById(id);
        kurikulum.setNamaKurikulum(updatedKurikulum.getNamaKurikulum());
        kurikulum.setKeterangan(updatedKurikulum.getKeterangan());
        kurikulum.setAktif(updatedKurikulum.isAktif());
        kurikulumRepository.save(kurikulum);
    }

    public void deleteKurikulum(Long id) {
        kurikulumRepository.deleteById(id);
    }

    public void assignKurikulumToKelas(Long kelasId, Long kurikulumId) {
        Kelas kelas = findKelasById(kelasId);
        Kurikulum kurikulum = getKurikulumById(kurikulumId);
        kelas.setKurikulum(kurikulum);
        kelasRepository.save(kelas);
    }

    // --- KELAS ---
    public List<Kelas> getAllKelas() {
        return kelasRepository.findAll();
    }

    public Kelas findKelasById(Long id) {
        return kelasRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Kelas tidak ditemukan"));
    }

    public void saveKelas(Kelas kelas) {
        kelasRepository.save(kelas);
    }

    public void updateKelas(Long id, String namaKelas, String tahunAjaran) {
        Kelas kelas = findKelasById(id);
        kelas.setNamaKelas(namaKelas);
        kelas.setTahunAjaran(tahunAjaran);
        kelasRepository.save(kelas);
    }

    public void deleteKelas(Long id) {
        kelasRepository.deleteById(id);
    }

    // --- MAPEL ---
    public List<MataPelajaran> getAllMapel() {
        return mapelRepository.findAll();
    }

    public MataPelajaran findMapelById(Long id) {
        return mapelRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Mapel tidak ditemukan"));
    }

    public void saveMapel(MataPelajaran mapel) {
        mapelRepository.save(mapel);
    }

    public void updateMapel(Long id, String namaMapel) {
        MataPelajaran mapel = findMapelById(id);
        mapel.setNamaMapel(namaMapel);
        mapelRepository.save(mapel);
    }

    public void deleteMapel(Long id) {
        mapelRepository.deleteById(id);
    }
}
