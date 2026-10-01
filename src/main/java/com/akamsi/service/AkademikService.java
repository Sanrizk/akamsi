package com.akamsi.service;

import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.MataPelajaranRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AkademikService {
    private final KelasRepository kelasRepository;
    private final MataPelajaranRepository mapelRepository;

    public AkademikService(KelasRepository kelasRepository, MataPelajaranRepository mapelRepository) {
        this.kelasRepository = kelasRepository;
        this.mapelRepository = mapelRepository;
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
