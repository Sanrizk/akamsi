package com.akamsi.repository;

import com.akamsi.entity.NilaiSiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NilaiSiswaRepository extends JpaRepository<NilaiSiswa, Long> {
    List<NilaiSiswa> findBySiswaIdAndKomponenNilai_Semester(Long siswaId, String semester);
    List<NilaiSiswa> findByKomponenNilaiId(Long komponenNilaiId);
    Optional<NilaiSiswa> findBySiswaIdAndKomponenNilaiId(Long siswaId, Long komponenNilaiId);
}
