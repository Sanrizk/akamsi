package com.akamsi.service;

import com.akamsi.dto.LaporanSppFilterDTO;
import com.akamsi.dto.LaporanSppSummaryDTO;
import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.StatusPembayaran;
import com.akamsi.repository.PembayaranSPPRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaporanSppService {

    @Autowired
    private PembayaranSPPRepository pembayaranSPPRepository;

    @Transactional(readOnly = true)
    public List<PembayaranSPP> getFilteredLaporan(LaporanSppFilterDTO filter) {
        String bulanFilter = (filter.getBulan() != null && !filter.getBulan().trim().isEmpty())
                ? filter.getBulan().trim()
                : null;

        return pembayaranSPPRepository.filterLaporan(
                filter.getKelasId(),
                filter.getTahun(),
                bulanFilter,
                filter.getStatus()
        );
    }

    public LaporanSppSummaryDTO calculateSummary(List<PembayaranSPP> list) {
        BigDecimal totalPemasukan = BigDecimal.ZERO;
        BigDecimal totalTunggakan = BigDecimal.ZERO;
        BigDecimal totalBatal = BigDecimal.ZERO;
        long jumlahLunas = 0;
        long jumlahBelumLunas = 0;
        long jumlahBatal = 0;

        for (PembayaranSPP spp : list) {
            BigDecimal nominal = (spp.getJumlahBayar() != null) ? spp.getJumlahBayar() : BigDecimal.ZERO;

            if (spp.getStatus() == StatusPembayaran.LUNAS) {
                totalPemasukan = totalPemasukan.add(nominal);
                jumlahLunas++;
            } else if (spp.getStatus() == StatusPembayaran.BELUM_LUNAS) {
                totalTunggakan = totalTunggakan.add(nominal);
                jumlahBelumLunas++;
            } else if (spp.getStatus() == StatusPembayaran.BATAL) {
                totalBatal = totalBatal.add(nominal);
                jumlahBatal++;
            }
        }

        return new LaporanSppSummaryDTO(
                totalPemasukan,
                totalTunggakan,
                totalBatal,
                jumlahLunas,
                jumlahBelumLunas,
                jumlahBatal,
                list.size()
        );
    }
}
