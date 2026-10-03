package com.akamsi.repository;

import com.akamsi.entity.Pengumuman;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PengumumanRepositoryTest {

    @Autowired
    private PengumumanRepository pengumumanRepository;

    @BeforeEach
    public void setup() {
        Pengumuman p1 = new Pengumuman();
        p1.setJudul("Pengumuman Umum");
        p1.setIsi("Isi umum");
        p1.setTargetRole("SEMUA");
        p1.setPenulis("Admin");
        p1.setCreatedAt(LocalDateTime.now().minusDays(2));
        pengumumanRepository.save(p1);

        Pengumuman p2 = new Pengumuman();
        p2.setJudul("Pengumuman Guru");
        p2.setIsi("Isi guru");
        p2.setTargetRole("GURU");
        p2.setPenulis("Admin");
        p2.setCreatedAt(LocalDateTime.now().minusDays(1));
        pengumumanRepository.save(p2);

        Pengumuman p3 = new Pengumuman();
        p3.setJudul("Pengumuman Siswa");
        p3.setIsi("Isi siswa");
        p3.setTargetRole("SISWA");
        p3.setPenulis("Admin");
        p3.setCreatedAt(LocalDateTime.now());
        pengumumanRepository.save(p3);
    }

    @Test
    public void testFindForGuru_ShouldReturnSemuaAndGuruOrderedByDateDesc() {
        List<Pengumuman> results = pengumumanRepository.findForGuru();
        Assertions.assertThat(results).hasSize(2);
        Assertions.assertThat(results).extracting(Pengumuman::getTargetRole)
                  .containsExactlyInAnyOrder("SEMUA", "GURU");
        Assertions.assertThat(results.get(0).getTargetRole()).isEqualTo("GURU"); 
        Assertions.assertThat(results.get(1).getTargetRole()).isEqualTo("SEMUA");
    }

    @Test
    public void testFindForSiswa_ShouldReturnSemuaAndSiswaOrderedByDateDesc() {
        List<Pengumuman> results = pengumumanRepository.findForSiswa();
        Assertions.assertThat(results).hasSize(2);
        Assertions.assertThat(results).extracting(Pengumuman::getTargetRole)
                  .containsExactlyInAnyOrder("SEMUA", "SISWA");
        Assertions.assertThat(results.get(0).getTargetRole()).isEqualTo("SISWA"); 
        Assertions.assertThat(results.get(1).getTargetRole()).isEqualTo("SEMUA"); 
    }
}

