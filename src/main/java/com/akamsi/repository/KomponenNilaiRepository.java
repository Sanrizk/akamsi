package com.akamsi.repository;

import com.akamsi.entity.KomponenNilai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KomponenNilaiRepository extends JpaRepository<KomponenNilai, Long> {
    List<KomponenNilai> findByKelasIdAndMapelId(Long kelasId, Long mapelId);
}
