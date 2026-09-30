package com.akamsi.repository;

import com.akamsi.entity.Presensi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PresensiRepository extends JpaRepository<Presensi, Long> {
    List<Presensi> findBySiswaIdAndTanggalBetween(Long siswaId, LocalDate startDate, LocalDate endDate);
    List<Presensi> findBySiswaIdOrderByTanggalDesc(Long siswaId);
}
