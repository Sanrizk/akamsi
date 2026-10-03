package com.akamsi.service;

import com.akamsi.dto.TeachingTaskDTO;
import com.akamsi.dto.TimeSlotDTO;
import com.akamsi.entity.Guru;
import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.GuruRepository;
import com.akamsi.repository.JadwalPelajaranRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JadwalGeneratorService {

    @Autowired
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @Autowired
    private AkademikService akademikService;

    @Autowired
    private GuruRepository guruRepository;

    public List<TimeSlotDTO> buildStandardTimeSlots() {
        List<TimeSlotDTO> slots = new ArrayList<>();
        String[] hariList = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat"};

        LocalTime[] jamMulai = {
            LocalTime.of(7, 30),
            LocalTime.of(8, 15),
            LocalTime.of(9, 0),
            LocalTime.of(10, 15),
            LocalTime.of(11, 0),
            LocalTime.of(12, 30),
            LocalTime.of(13, 15),
            LocalTime.of(14, 0)
        };

        LocalTime[] jamSelesai = {
            LocalTime.of(8, 15),
            LocalTime.of(9, 0),
            LocalTime.of(9, 45),
            LocalTime.of(11, 0),
            LocalTime.of(11, 45),
            LocalTime.of(13, 15),
            LocalTime.of(14, 0),
            LocalTime.of(14, 45)
        };

        for (String hari : hariList) {
            int maxJp = hari.equals("Jumat") ? 5 : 8;
            for (int jp = 1; jp <= maxJp; jp++) {
                slots.add(new TimeSlotDTO(hari, jp, jamMulai[jp - 1], jamSelesai[jp - 1]));
            }
        }

        return slots;
    }

    @Transactional
    public int generateJadwalOtomatis(boolean resetJadwalLama) {
        List<Kelas> kelasList = akademikService.getAllKelas();
        List<MataPelajaran> mapelList = akademikService.getAllMapel();
        List<Guru> guruList = guruRepository.findAll();

        if (kelasList.isEmpty() || mapelList.isEmpty() || guruList.isEmpty()) {
            throw new IllegalStateException("Data Kelas, Mata Pelajaran, atau Guru masih kosong.");
        }

        if (resetJadwalLama) {
            jadwalPelajaranRepository.deleteAll();
        }

        List<TeachingTaskDTO> tasks = new ArrayList<>();
        int guruIndex = 0;

        for (Kelas k : kelasList) {
            for (MataPelajaran m : mapelList) {
                Guru assignedGuru = guruList.get(guruIndex % guruList.size());
                guruIndex++;
                tasks.add(new TeachingTaskDTO(k, m, assignedGuru, 2));
            }
        }

        Collections.shuffle(tasks);

        List<TimeSlotDTO> timeSlots = buildStandardTimeSlots();

        Set<String> occupiedTeacherSlots = new HashSet<>();
        Set<String> occupiedClassSlots = new HashSet<>();
        List<JadwalPelajaran> scheduledList = new ArrayList<>();

        boolean success = backtrackSchedule(0, tasks, timeSlots, occupiedTeacherSlots, occupiedClassSlots, scheduledList);

        if (!success) {
            throw new RuntimeException("Tidak dapat menemukan kombinasi jadwal tanpa bentrok. Tambah guru pengampu atau kurangi beban jam.");
        }

        jadwalPelajaranRepository.saveAll(scheduledList);
        return scheduledList.size();
    }

    private boolean backtrackSchedule(
            int taskIndex,
            List<TeachingTaskDTO> tasks,
            List<TimeSlotDTO> timeSlots,
            Set<String> occupiedTeacherSlots,
            Set<String> occupiedClassSlots,
            List<JadwalPelajaran> scheduledList) {

        if (taskIndex >= tasks.size()) {
            return true;
        }

        TeachingTaskDTO task = tasks.get(taskIndex);
        Long guruId = task.getGuru().getId();
        Long kelasId = task.getKelas().getId();

        for (int i = 0; i < timeSlots.size() - 1; i++) {
            TimeSlotDTO slot1 = timeSlots.get(i);
            TimeSlotDTO slot2 = timeSlots.get(i + 1);

            if (!slot1.getHari().equals(slot2.getHari()) || slot2.getJamKe() != slot1.getJamKe() + 1) {
                continue;
            }

            String teacherKey1 = guruId + ":" + slot1.getSlotKey();
            String teacherKey2 = guruId + ":" + slot2.getSlotKey();
            String classKey1 = kelasId + ":" + slot1.getSlotKey();
            String classKey2 = kelasId + ":" + slot2.getSlotKey();

            if (occupiedTeacherSlots.contains(teacherKey1) || occupiedTeacherSlots.contains(teacherKey2)
                    || occupiedClassSlots.contains(classKey1) || occupiedClassSlots.contains(classKey2)) {
                continue;
            }

            occupiedTeacherSlots.add(teacherKey1);
            occupiedTeacherSlots.add(teacherKey2);
            occupiedClassSlots.add(classKey1);
            occupiedClassSlots.add(classKey2);

            JadwalPelajaran j = new JadwalPelajaran();
            j.setKelas(task.getKelas());
            j.setMapel(task.getMapel());
            j.setGuru(task.getGuru());
            j.setHari(com.akamsi.entity.Hari.valueOf(slot1.getHari().toUpperCase()));
            j.setJamMulai(slot1.getJamMulai());
            j.setJamSelesai(slot2.getJamSelesai());

            scheduledList.add(j);

            if (backtrackSchedule(taskIndex + 1, tasks, timeSlots, occupiedTeacherSlots, occupiedClassSlots, scheduledList)) {
                return true;
            }

            scheduledList.remove(scheduledList.size() - 1);
            occupiedTeacherSlots.remove(teacherKey1);
            occupiedTeacherSlots.remove(teacherKey2);
            occupiedClassSlots.remove(classKey1);
            occupiedClassSlots.remove(classKey2);
        }

        return false;
    }
}
