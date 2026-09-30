package com.akamsi.repository;

import com.akamsi.entity.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface SiswaRepository extends JpaRepository<Siswa, Long> {
    List<Siswa> findByKelasId(Long kelasId);
    Optional<Siswa> findByUserId(Long userId);
}
