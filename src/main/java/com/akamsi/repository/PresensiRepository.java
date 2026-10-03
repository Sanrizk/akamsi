package com.akamsi.repository;

import com.akamsi.entity.Presensi;
import com.akamsi.entity.StatusPresensi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresensiRepository extends JpaRepository<Presensi, Long> {
    Optional<Presensi> findBySiswaIdAndMapelIdAndTanggal(Long siswaId, Long mapelId, LocalDate tanggal);
    List<Presensi> findBySiswaKelasIdAndMapelIdAndTanggal(Long kelasId, Long mapelId, LocalDate tanggal);
    List<Presensi> findBySiswaIdOrderByTanggalDesc(Long siswaId);

    @Query("SELECT COUNT(p) FROM Presensi p WHERE p.tanggal = :tanggal")
    long countByTanggal(@Param("tanggal") LocalDate tanggal);

    @Query("SELECT COUNT(p) FROM Presensi p WHERE p.tanggal = :tanggal AND p.status = :status")
    long countByTanggalAndStatus(@Param("tanggal") LocalDate tanggal, @Param("status") StatusPresensi status);

    @Query("SELECT p FROM Presensi p WHERE p.siswa.id = :siswaId AND p.tanggal BETWEEN :startDate AND :endDate ORDER BY p.tanggal DESC")
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

    @Query("SELECT p FROM Presensi p WHERE p.siswa.id = :siswaId AND p.mapel.id = :mapelId AND p.tanggal BETWEEN :startDate AND :endDate ORDER BY p.tanggal DESC")
    List<Presensi> findBySiswaIdAndMapelIdAndTanggalBetween(
            @org.springframework.data.repository.query.Param("siswaId") Long siswaId,
            @org.springframework.data.repository.query.Param("mapelId") Long mapelId,
            @org.springframework.data.repository.query.Param("startDate") LocalDate startDate,
            @org.springframework.data.repository.query.Param("endDate") LocalDate endDate
    );

}
