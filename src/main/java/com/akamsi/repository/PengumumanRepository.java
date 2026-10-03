package com.akamsi.repository;

import com.akamsi.entity.Pengumuman;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PengumumanRepository extends JpaRepository<Pengumuman, Long> {

    @Query("SELECT p FROM Pengumuman p WHERE p.targetRole IN ('SEMUA', 'GURU') ORDER BY p.createdAt DESC")
    List<Pengumuman> findForGuru();

    @Query("SELECT p FROM Pengumuman p WHERE p.targetRole IN ('SEMUA', 'SISWA') ORDER BY p.createdAt DESC")
    List<Pengumuman> findForSiswa();
}

