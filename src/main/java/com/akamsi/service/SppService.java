package com.akamsi.service;

import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.StatusPembayaran;
import com.akamsi.entity.User;
import com.akamsi.repository.PembayaranSPPRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SppService {
    private final PembayaranSPPRepository sppRepository;

    public SppService(PembayaranSPPRepository sppRepository) {
        this.sppRepository = sppRepository;
    }

    public List<PembayaranSPP> getAllPembayaran() {
        return sppRepository.findAll();
    }

    public PembayaranSPP findById(Long id) {
        return sppRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Data SPP tidak ditemukan"));
    }

    public void processManualPayment(Long pembayaranId, User admin, String catatan) {
        PembayaranSPP spp = findById(pembayaranId);
        spp.setStatus(StatusPembayaran.LUNAS);
        spp.setTanggalBayar(LocalDate.now());
        spp.setAdmin(admin);
        spp.setCatatan(catatan);
        sppRepository.save(spp);
    }

    public void voidPayment(Long id) {
        PembayaranSPP spp = findById(id);
        spp.setStatus(StatusPembayaran.BELUM_LUNAS);
        spp.setTanggalBayar(null);
        spp.setAdmin(null);
        spp.setCatatan("Dibatalkan");
        sppRepository.save(spp);
    }
}
