package com.akamsi.service;

import com.akamsi.entity.Kelas;
import com.akamsi.entity.KomponenNilai;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.entity.NilaiSiswa;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.KomponenNilaiRepository;
import com.akamsi.repository.NilaiSiswaRepository;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.SiswaRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GuruRaporServiceTest {

    @Mock
    private RaporAkhirRepository raporAkhirRepository;

    @Mock
    private SiswaRepository siswaRepository;

    @Mock
    private KomponenNilaiRepository komponenNilaiRepository;

    @Mock
    private NilaiSiswaRepository nilaiSiswaRepository;

    @Mock
    private AkademikService akademikService;

    @InjectMocks
    private GuruRaporService guruRaporService;

    @Captor
    private ArgumentCaptor<RaporAkhir> raporCaptor;

    private Siswa siswa;
    private Kelas kelas;
    private MataPelajaran mapel;
    private KomponenNilai komponen1;
    private KomponenNilai komponen2;

    @BeforeEach
    public void setup() {
        kelas = new Kelas();
        kelas.setId(1L);
        kelas.setNamaKelas("Kelas 4A");

        siswa = new Siswa();
        siswa.setId(10L);
        siswa.setKelas(kelas);

        mapel = new MataPelajaran();
        mapel.setId(100L);

        komponen1 = new KomponenNilai();
        komponen1.setId(1001L);

        komponen2 = new KomponenNilai();
        komponen2.setId(1002L);
    }

    @Test
    public void testKalkulasiRaporSiswa_ShouldCalculateAverageAndAssignPredikatA() {
        Mockito.when(siswaRepository.findById(10L)).thenReturn(Optional.of(siswa));
        Mockito.when(akademikService.findKelasById(1L)).thenReturn(kelas);
        Mockito.when(akademikService.getAllMapel()).thenReturn(List.of(mapel));
        Mockito.when(komponenNilaiRepository.findByKelasIdAndMapelId(1L, 100L))
               .thenReturn(List.of(komponen1, komponen2));

        NilaiSiswa ns1 = new NilaiSiswa(); ns1.setNilai(90.0);
        NilaiSiswa ns2 = new NilaiSiswa(); ns2.setNilai(100.0);

        Mockito.when(nilaiSiswaRepository.findByKomponenNilaiIdAndSiswaId(1001L, 10L))
               .thenReturn(Optional.of(ns1));
        Mockito.when(nilaiSiswaRepository.findByKomponenNilaiIdAndSiswaId(1002L, 10L))
               .thenReturn(Optional.of(ns2));

        Mockito.when(raporAkhirRepository.findBySiswaIdAndNamaKelasAndMapelIdAndSemester(10L, "Kelas 4A", 100L, 1))
               .thenReturn(Optional.empty());

        guruRaporService.kalkulasiRaporSiswa(10L, 1L, 1);

        Mockito.verify(raporAkhirRepository, Mockito.times(1)).save(raporCaptor.capture());
        RaporAkhir savedRapor = raporCaptor.getValue();
        
        Assertions.assertThat(savedRapor.getNilaiAkhir()).isEqualTo(95.0);
        Assertions.assertThat(savedRapor.getPredikat()).isEqualTo("A");
        Assertions.assertThat(savedRapor.getNamaKelas()).isEqualTo("Kelas 4A");
        Assertions.assertThat(savedRapor.getDeskripsiCapaian()).contains("sangat mendalam");
    }

    @Test
    public void testKalkulasiRaporSiswa_ShouldCalculateAverageAndAssignPredikatC() {
        Mockito.when(siswaRepository.findById(10L)).thenReturn(Optional.of(siswa));
        Mockito.when(akademikService.findKelasById(1L)).thenReturn(kelas);
        Mockito.when(akademikService.getAllMapel()).thenReturn(List.of(mapel));
        Mockito.when(komponenNilaiRepository.findByKelasIdAndMapelId(1L, 100L))
               .thenReturn(List.of(komponen1, komponen2));

        NilaiSiswa ns1 = new NilaiSiswa(); ns1.setNilai(60.0);
        NilaiSiswa ns2 = new NilaiSiswa(); ns2.setNilai(70.0);

        Mockito.when(nilaiSiswaRepository.findByKomponenNilaiIdAndSiswaId(1001L, 10L))
               .thenReturn(Optional.of(ns1));
        Mockito.when(nilaiSiswaRepository.findByKomponenNilaiIdAndSiswaId(1002L, 10L))
               .thenReturn(Optional.of(ns2));

        Mockito.when(raporAkhirRepository.findBySiswaIdAndNamaKelasAndMapelIdAndSemester(10L, "Kelas 4A", 100L, 2))
               .thenReturn(Optional.empty());

        guruRaporService.kalkulasiRaporSiswa(10L, 1L, 2);

        Mockito.verify(raporAkhirRepository, Mockito.times(1)).save(raporCaptor.capture());
        RaporAkhir savedRapor = raporCaptor.getValue();
        
        Assertions.assertThat(savedRapor.getNilaiAkhir()).isEqualTo(65.0);
        Assertions.assertThat(savedRapor.getPredikat()).isEqualTo("C");
        Assertions.assertThat(savedRapor.getDeskripsiCapaian()).contains("ketuntasan minimal");
    }
}

