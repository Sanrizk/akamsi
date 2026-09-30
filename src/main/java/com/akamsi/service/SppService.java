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

    public void processManualPayment(Long pembayaranId, User admin, String catatan) {
        PembayaranSPP spp = sppRepository.findById(pembayaranId)
            .orElseThrow(() -> new IllegalArgumentException("Data pembayaran tidak ditemukan!"));
            
        spp.setStatus(StatusPembayaran.LUNAS);
        spp.setTanggalBayar(LocalDate.now());
        spp.setAdmin(admin); // records the admin_id automatically via JPA relation
        spp.setCatatan(catatan);
        
        sppRepository.save(spp);
    }
}
