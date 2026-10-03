package com.akamsi.service;

import com.akamsi.dto.ProgresNilaiKelasDTO;
import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.entity.RaporAkhir;
import com.akamsi.entity.Siswa;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.repository.RaporAkhirRepository;
import com.akamsi.repository.SiswaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminRaporService {

    @Autowired
    private AkademikService akademikService;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @Autowired
    private RaporAkhirRepository raporAkhirRepository;

    @Transactional(readOnly = true)
    public List<ProgresNilaiKelasDTO> getProgresNilaiByKelas(Long kelasId, Integer semester) {
        Kelas kelas = akademikService.findKelasById(kelasId);
        if (kelas == null) {
            return new ArrayList<>();
        }

        List<Siswa> siswaList = siswaRepository.findByKelasId(kelasId);
        int totalSiswa = siswaList.size();

        List<MataPelajaran> mapelList = akademikService.getAllMapel();
        List<ProgresNilaiKelasDTO> progresList = new ArrayList<>();

        for (MataPelajaran mapel : mapelList) {
            List<JadwalPelajaran> jadwalList = jadwalPelajaranRepository.findByKelasIdAndMapelId(kelasId, mapel.getId());
            String namaGuru = "-";
            if (!jadwalList.isEmpty() && jadwalList.get(0).getGuru() != null && jadwalList.get(0).getGuru().getUser() != null) {
                namaGuru = jadwalList.get(0).getGuru().getUser().getNamaLengkap();
            }

            int countDinilai = 0;
            if (totalSiswa > 0) {
                for (Siswa s : siswaList) {
                    if (raporAkhirRepository.findBySiswaIdAndNamaKelasAndMapelIdAndSemester(
                            s.getId(), kelas.getNamaKelas(), mapel.getId(), semester).isPresent()) {
                        countDinilai++;
                    }
                }
            }

            int persentase = (totalSiswa > 0) ? (int) Math.round(((double) countDinilai / totalSiswa) * 100.0) : 0;
            String status;
            if (totalSiswa == 0) {
                status = "KOSONG";
            } else if (persentase >= 100) {
                status = "LENGKAP";
            } else if (persentase > 0) {
                status = "PROSES";
            } else {
                status = "BELUM";
            }

            progresList.add(new ProgresNilaiKelasDTO(
                    mapel.getId(),
                    mapel.getNamaMapel(),
                    namaGuru,
                    totalSiswa,
                    countDinilai,
                    persentase,
                    status
            ));
        }

        return progresList;
    }

    @Transactional(readOnly = true)
    public List<RaporAkhir> getRaporSiswa(Long siswaId, String namaKelas, Integer semester) {
        return raporAkhirRepository.findBySiswaIdAndNamaKelasAndSemester(siswaId, namaKelas, semester);
    }
}
