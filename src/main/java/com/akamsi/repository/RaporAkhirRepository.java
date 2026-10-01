package com.akamsi.repository;

import com.akamsi.entity.RaporAkhir;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RaporAkhirRepository extends JpaRepository<RaporAkhir, Long> {
    List<RaporAkhir> findBySiswaIdAndSemester(Long siswaId, String semester);
    List<RaporAkhir> findBySiswaId(Long siswaId);
}
