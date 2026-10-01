package com.akamsi.repository;

import com.akamsi.entity.Presensi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresensiRepository extends JpaRepository<Presensi, Long> {
    Optional<Presensi> findBySiswaIdAndMapelIdAndTanggal(Long siswaId, Long mapelId, LocalDate tanggal);
    List<Presensi> findBySiswaKelasIdAndMapelIdAndTanggal(Long kelasId, Long mapelId, LocalDate tanggal);
    List<Presensi> findBySiswaIdOrderByTanggalDesc(Long siswaId);
}
