package com.akamsi.service;

import com.akamsi.dto.TimeSlotDTO;
import com.akamsi.entity.Guru;
import com.akamsi.entity.Hari;
import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.GuruRepository;
import com.akamsi.repository.JadwalPelajaranRepository;
import java.util.ArrayList;
import java.util.List;
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
public class JadwalGeneratorServiceTest {

    @Mock
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @Mock
    private AkademikService akademikService;

    @Mock
    private GuruRepository guruRepository;

    @InjectMocks
    private JadwalGeneratorService jadwalGeneratorService;

    @Captor
    private ArgumentCaptor<List<JadwalPelajaran>> jadwalListCaptor;

    private Kelas kelas1;
    private Kelas kelas2;
    private MataPelajaran mapel1;
    private MataPelajaran mapel2;
    private Guru guru1;
    private Guru guru2;

    @BeforeEach
    public void setup() {
        kelas1 = new Kelas();
        kelas1.setId(1L);
        kelas1.setNamaKelas("Kelas 1A");

        kelas2 = new Kelas();
        kelas2.setId(2L);
        kelas2.setNamaKelas("Kelas 1B");

        mapel1 = new MataPelajaran();
        mapel1.setId(1L);
        mapel1.setNamaMapel("Matematika");

        mapel2 = new MataPelajaran();
        mapel2.setId(2L);
        mapel2.setNamaMapel("Bahasa Indonesia");

        guru1 = new Guru();
        guru1.setId(1L);

        guru2 = new Guru();
        guru2.setId(2L);
    }

    @Test
    public void testBuildStandardTimeSlots_ShouldReturnCorrectSizes() {
        List<TimeSlotDTO> slots = jadwalGeneratorService.buildStandardTimeSlots();
        Assertions.assertThat(slots).isNotEmpty();
        Assertions.assertThat(slots).hasSize(37);
        long jumatCount = slots.stream().filter(s -> s.getHari().equals("Jumat")).count();
        Assertions.assertThat(jumatCount).isEqualTo(5);
    }

    @Test
    public void testGenerateJadwalOtomatis_EmptyData_ShouldThrowException() {
        Mockito.when(akademikService.getAllKelas()).thenReturn(new ArrayList<>());
        Assertions.assertThatThrownBy(() -> jadwalGeneratorService.generateJadwalOtomatis(true))
                  .isInstanceOf(IllegalStateException.class)
                  .hasMessageContaining("Data Kelas, Mata Pelajaran, atau Guru masih kosong.");
    }

    @Test
    public void testGenerateJadwalOtomatis_ValidData_ShouldGenerateWithoutCollisions() {
        List<Kelas> kelasList = List.of(kelas1, kelas2);
        List<MataPelajaran> mapelList = List.of(mapel1, mapel2);
        List<Guru> guruList = List.of(guru1, guru2);

        Mockito.when(akademikService.getAllKelas()).thenReturn(kelasList);
        Mockito.when(akademikService.getAllMapel()).thenReturn(mapelList);
        Mockito.when(guruRepository.findAll()).thenReturn(guruList);

        int generatedCount = jadwalGeneratorService.generateJadwalOtomatis(true);

        Mockito.verify(jadwalPelajaranRepository, Mockito.times(1)).deleteAll();
        Mockito.verify(jadwalPelajaranRepository, Mockito.times(1)).saveAll(jadwalListCaptor.capture());

        List<JadwalPelajaran> savedJadwal = jadwalListCaptor.getValue();
        Assertions.assertThat(savedJadwal).hasSize(4);

        for (int i = 0; i < savedJadwal.size(); i++) {
            for (int j = i + 1; j < savedJadwal.size(); j++) {
                JadwalPelajaran j1 = savedJadwal.get(i);
                JadwalPelajaran j2 = savedJadwal.get(j);

                boolean isSameDay = j1.getHari() == j2.getHari();
                boolean isTimeOverlap = j1.getJamMulai().equals(j2.getJamMulai());

                if (isSameDay && isTimeOverlap) {
                    Assertions.assertThat(j1.getGuru().getId()).isNotEqualTo(j2.getGuru().getId());
                    Assertions.assertThat(j1.getKelas().getId()).isNotEqualTo(j2.getKelas().getId());
                }
            }
        }
    }
}

