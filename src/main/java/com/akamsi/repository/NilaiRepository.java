package com.akamsi.repository;

import com.akamsi.entity.Nilai;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NilaiRepository extends JpaRepository<Nilai, Long> {
    List<Nilai> findBySiswaIdAndSemester(Long siswaId, String semester);
    Optional<Nilai> findBySiswaIdAndMapelIdAndSemester(Long siswaId, Long mapelId, String semester);
}
