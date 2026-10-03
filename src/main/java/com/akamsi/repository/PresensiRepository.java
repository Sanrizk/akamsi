package com.akamsi.repository;

import com.akamsi.entity.Presensi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresensiRepository extends JpaRepository<Presensi, Long> {
    Optional<Presensi> findBySiswaIdAndMapelIdAndTanggal(Long siswaId, Long mapelId, LocalDate tanggal);
    List<Presensi> findBySiswaKelasIdAndMapelIdAndTanggal(Long kelasId, Long mapelId, LocalDate tanggal);
    List<Presensi> findBySiswaIdOrderByTanggalDesc(Long siswaId);

    @Query("SELECT p FROM Presensi p WHERE p.siswa.id = :siswaId AND p.tanggal BETWEEN :startDate AND :endDate")
    List<Presensi> findBySiswaIdAndTanggalBetween(
            @org.springframework.data.repository.query.Param("siswaId") Long siswaId,
            @org.springframework.data.repository.query.Param("startDate") LocalDate startDate,
            @org.springframework.data.repository.query.Param("endDate") LocalDate endDate
    );

    @Query("SELECT p FROM Presensi p WHERE p.siswa.kelas.id = :kelasId AND p.tanggal BETWEEN :startDate AND :endDate")
    List<Presensi> findByKelasIdAndTanggalBetween(
            @org.springframework.data.repository.query.Param("kelasId") Long kelasId,
            @org.springframework.data.repository.query.Param("startDate") LocalDate startDate,
            @org.springframework.data.repository.query.Param("endDate") LocalDate endDate
    );
}
