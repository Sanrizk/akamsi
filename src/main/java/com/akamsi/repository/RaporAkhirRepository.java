package com.akamsi.repository;

import com.akamsi.entity.RaporAkhir;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RaporAkhirRepository extends JpaRepository<RaporAkhir, Long> {

    @Query("SELECT r FROM RaporAkhir r JOIN FETCH r.mapel WHERE r.siswa.id = :siswaId AND r.semester = :semester ORDER BY r.mapel.namaMapel ASC")
    List<RaporAkhir> findBySiswaIdAndSemester(@Param("siswaId") Long siswaId, @Param("semester") Integer semester);

    Optional<RaporAkhir> findBySiswaIdAndMapelIdAndSemester(Long siswaId, Long mapelId, Integer semester);

    @Modifying
    @Query("DELETE FROM RaporAkhir r WHERE r.siswa.id = :siswaId AND r.semester = :semester")
    void deleteBySiswaIdAndSemester(@Param("siswaId") Long siswaId, @Param("semester") Integer semester);
}
