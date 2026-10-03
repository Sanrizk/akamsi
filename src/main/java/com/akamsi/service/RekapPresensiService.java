package com.akamsi.service;

import com.akamsi.dto.RekapPresensiKelasSummaryDTO;
import com.akamsi.dto.RekapPresensiSiswaDTO;
import com.akamsi.entity.Presensi;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.StatusPresensi;
import com.akamsi.repository.PresensiRepository;
import com.akamsi.repository.SiswaRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RekapPresensiService {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PresensiRepository presensiRepository;

    @Transactional(readOnly = true)
    public List<RekapPresensiSiswaDTO> getRekapBulananKelas(Long kelasId, int bulan, int tahun) {
        List<Siswa> siswaList = siswaRepository.findByKelasId(kelasId);
        YearMonth ym = YearMonth.of(tahun, bulan);
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        List<Presensi> presensiBulanIni = presensiRepository.findByKelasIdAndTanggalBetween(kelasId, startDate, endDate);
        List<RekapPresensiSiswaDTO> result = new ArrayList<>();

        for (Siswa s : siswaList) {
            int h = 0;
            int sc = 0;
            int iz = 0;
            int a = 0;

            for (Presensi p : presensiBulanIni) {
                if (p.getSiswa().getId().equals(s.getId())) {
                    if (p.getStatus() == StatusPresensi.HADIR) {
                        h++;
                    } else if (p.getStatus() == StatusPresensi.SAKIT) {
                        sc++;
                    } else if (p.getStatus() == StatusPresensi.IZIN) {
                        iz++;
                    } else if (p.getStatus() == StatusPresensi.ALPA) {
                        a++;
                    }
                }
            }

            int total = h + sc + iz + a;
            double persentase = (total > 0) ? Math.round(((double) h / total * 100.0) * 10.0) / 10.0 : 100.0;

            result.add(new RekapPresensiSiswaDTO(
                    s.getId(),
                    s.getNisn(),
                    s.getNamaSiswa(),
                    h,
                    sc,
                    iz,
                    a,
                    total,
                    persentase
            ));
        }

        return result;
    }

    public RekapPresensiKelasSummaryDTO calculateSummary(List<RekapPresensiSiswaDTO> list) {
        if (list.isEmpty()) {
            return new RekapPresensiKelasSummaryDTO(0, 100.0, 0, 0, 0);
        }

        int totalSiswa = list.size();
        double totalPersentase = 0.0;
        int siswaKritis = 0;
        int totalHadir = 0;
        int totalAlpa = 0;

        for (RekapPresensiSiswaDTO dto : list) {
            totalPersentase += dto.getPersentaseKehadiran();
            totalHadir += dto.getHadir();
            totalAlpa += dto.getAlpa();

            if (dto.getPersentaseKehadiran() < 75.0 || dto.getAlpa() >= 3) {
                siswaKritis++;
            }
        }

        double rataRata = Math.round((totalPersentase / totalSiswa) * 10.0) / 10.0;

        return new RekapPresensiKelasSummaryDTO(totalSiswa, rataRata, siswaKritis, totalHadir, totalAlpa);
    }
}
