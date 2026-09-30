package com.akamsi.repository;

import com.akamsi.entity.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KelasRepository extends JpaRepository<Kelas, Long> {
    Optional<Kelas> findByWaliKelasId(Long guruId);
}
