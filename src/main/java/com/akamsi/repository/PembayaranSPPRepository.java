package com.akamsi.repository;

import com.akamsi.entity.PembayaranSPP;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PembayaranSPPRepository extends JpaRepository<PembayaranSPP, Long> {

    List<PembayaranSPP> findBySiswaIdOrderByTahunAsc(Long siswaId);

    List<PembayaranSPP> findBySiswaId(Long siswaId);

    boolean existsBySiswaIdAndBulanAndTahun(Long siswaId, String bulan, Integer tahun);

    Optional<PembayaranSPP> findBySiswaIdAndBulanAndTahun(Long siswaId, String bulan, Integer tahun);
}
