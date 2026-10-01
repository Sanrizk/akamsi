package com.akamsi.repository;

import com.akamsi.entity.KomponenNilai;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KomponenNilaiRepository extends JpaRepository<KomponenNilai, Long> {
    List<KomponenNilai> findByKelasIdAndMapelIdAndSemester(Long kelasId, Long mapelId, String semester);
    List<KomponenNilai> findByKelasIdAndSemester(Long kelasId, String semester);
}
