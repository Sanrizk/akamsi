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

    @Query("SELECT j FROM JadwalPelajaran j WHERE j.guru.user.id = :userId AND LOWER(j.hari) = LOWER(:hari) ORDER BY j.jamMulai ASC")
    List<JadwalPelajaran> findByGuruUserIdAndHari(@Param("userId") Long userId, @Param("hari") String hari);

    @Query("SELECT j FROM JadwalPelajaran j WHERE j.guru.user.id = :userId ORDER BY j.hari ASC, j.jamMulai ASC")
    List<JadwalPelajaran> findByGuruUserId(@Param("userId") Long userId);

    List<JadwalPelajaran> findByKelasIdOrderByHariAscJamMulaiAsc(Long kelasId);

    @Query("SELECT DISTINCT j.kelas FROM JadwalPelajaran j WHERE j.guru.user.id = :userId ORDER BY j.kelas.namaKelas ASC")
    List<Kelas> findDistinctKelasByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT j.mapel FROM JadwalPelajaran j WHERE j.guru.user.id = :userId AND j.kelas.id = :kelasId ORDER BY j.mapel.namaMapel")
    List<MataPelajaran> findDistinctMapelByUserIdAndKelasId(@Param("userId") Long userId, @Param("kelasId") Long kelasId);

    List<JadwalPelajaran> findByGuruId(Long guruId);

    List<JadwalPelajaran> findByKelasIdAndMapelId(Long kelasId, Long mapelId);
}
