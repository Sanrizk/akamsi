package com.akamsi.repository;

import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.StatusPembayaran;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PembayaranSPPRepository extends JpaRepository<PembayaranSPP, Long> {

    List<PembayaranSPP> findBySiswaIdOrderByTahunAsc(Long siswaId);

    List<PembayaranSPP> findBySiswaId(Long siswaId);

    boolean existsBySiswaIdAndBulanAndTahun(Long siswaId, String bulan, Integer tahun);

    Optional<PembayaranSPP> findBySiswaIdAndBulanAndTahun(Long siswaId, String bulan, Integer tahun);

    @Query("SELECT p FROM PembayaranSPP p WHERE " +
           "(:kelasId IS NULL OR p.siswa.kelas.id = :kelasId) AND " +
           "(:tahun IS NULL OR p.tahun = :tahun) AND " +
           "(:bulan IS NULL OR :bulan = '' OR p.bulan = :bulan) AND " +
           "(:status IS NULL OR p.status = :status) " +
           "ORDER BY p.tahun DESC, p.siswa.namaSiswa ASC, p.id ASC")
    List<PembayaranSPP> filterLaporan(
            @Param("kelasId") Long kelasId,
            @Param("tahun") Integer tahun,
            @Param("bulan") String bulan,
            @Param("status") StatusPembayaran status
    );
}
