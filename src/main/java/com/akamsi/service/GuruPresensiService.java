package com.akamsi.service;

import com.akamsi.dto.PresensiBatchFormDTO;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.entity.Presensi;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.StatusPresensi;
import com.akamsi.repository.MataPelajaranRepository;
import com.akamsi.repository.PresensiRepository;
import com.akamsi.repository.SiswaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GuruPresensiService {

    private final PresensiRepository presensiRepository;
    private final SiswaRepository siswaRepository;
    private final MataPelajaranRepository mapelRepository;

    public GuruPresensiService(PresensiRepository presensiRepository, 
                               SiswaRepository siswaRepository,
                               MataPelajaranRepository mapelRepository) {
        this.presensiRepository = presensiRepository;
        this.siswaRepository = siswaRepository;
        this.mapelRepository = mapelRepository;
    }

    public PresensiBatchFormDTO getPresensiForm(Long kelasId, Long mapelId, LocalDate tanggal) {
        List<Siswa> siswaList = siswaRepository.findAll().stream()
                .filter(s -> s.getKelas() != null && s.getKelas().getId().equals(kelasId))
                .toList();

        PresensiBatchFormDTO form = new PresensiBatchFormDTO();
        form.setKelasId(kelasId);
        form.setMapelId(mapelId);
        form.setTanggal(tanggal);

        List<PresensiBatchFormDTO.ItemPresensiDTO> items = new ArrayList<>();
        for (Siswa s : siswaList) {
            Optional<Presensi> existing = presensiRepository.findBySiswaIdAndMapelIdAndTanggal(s.getId(), mapelId, tanggal);
            PresensiBatchFormDTO.ItemPresensiDTO item = new PresensiBatchFormDTO.ItemPresensiDTO();
            item.setSiswaId(s.getId());
            item.setNisn(s.getNisn());
            item.setNamaSiswa(s.getNamaSiswa());
            
            if (existing.isPresent()) {
                item.setStatus(existing.get().getStatus());
                item.setKeterangan(existing.get().getKeterangan());
            } else {
                item.setStatus(StatusPresensi.HADIR);
                item.setKeterangan("");
            }
            items.add(item);
        }
        form.setItems(items);
        return form;
    }

    public void saveBatchPresensi(PresensiBatchFormDTO form) {
        MataPelajaran mapel = mapelRepository.findById(form.getMapelId())
                .orElseThrow(() -> new RuntimeException("Mapel tidak ditemukan"));

        for (PresensiBatchFormDTO.ItemPresensiDTO item : form.getItems()) {
            Siswa siswa = siswaRepository.findById(item.getSiswaId())
                    .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));

            Presensi p = presensiRepository.findBySiswaIdAndMapelIdAndTanggal(siswa.getId(), mapel.getId(), form.getTanggal())
                    .orElse(new Presensi());
            
            p.setSiswa(siswa);
            p.setMapel(mapel);
            p.setTanggal(form.getTanggal());
            p.setStatus(item.getStatus());
            p.setKeterangan(item.getKeterangan());
            
            presensiRepository.save(p);
        }
    }
}
