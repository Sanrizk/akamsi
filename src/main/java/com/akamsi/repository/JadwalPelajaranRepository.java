package com.akamsi.repository;

import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JadwalPelajaranRepository extends JpaRepository<JadwalPelajaran, Long> {

    @Query("SELECT DISTINCT j.kelas FROM JadwalPelajaran j WHERE j.guru.user.id = :userId ORDER BY j.kelas.namaKelas")
    List<Kelas> findDistinctKelasByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT j.mapel FROM JadwalPelajaran j WHERE j.guru.user.id = :userId AND j.kelas.id = :kelasId ORDER BY j.mapel.namaMapel")
    List<MataPelajaran> findDistinctMapelByUserIdAndKelasId(@Param("userId") Long userId, @Param("kelasId") Long kelasId);

    List<JadwalPelajaran> findByGuruId(Long guruId);
}
