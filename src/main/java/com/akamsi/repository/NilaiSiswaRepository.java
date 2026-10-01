package com.akamsi.repository;

import com.akamsi.entity.NilaiSiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NilaiSiswaRepository extends JpaRepository<NilaiSiswa, Long> {
    List<NilaiSiswa> findByKomponenNilaiId(Long komponenNilaiId);
    List<NilaiSiswa> findBySiswaId(Long siswaId);
}
