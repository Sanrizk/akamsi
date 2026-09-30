package com.akamsi.repository;

import com.akamsi.entity.PembayaranSPP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PembayaranSPPRepository extends JpaRepository<PembayaranSPP, Long> {
    List<PembayaranSPP> findBySiswaId(Long siswaId);
}
