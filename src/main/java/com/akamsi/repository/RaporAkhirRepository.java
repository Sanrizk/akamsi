package com.akamsi.repository;

import com.akamsi.entity.RaporAkhir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaporAkhirRepository extends JpaRepository<RaporAkhir, Long> {
    List<RaporAkhir> findBySiswaIdAndSemester(Long siswaId, Integer semester);
}
