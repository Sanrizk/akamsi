package com.akamsi.service;

import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.StatusPembayaran;
import com.akamsi.repository.PembayaranSPPRepository;
import com.akamsi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class SppService {

    private final PembayaranSPPRepository sppRepository;
    private final UserRepository userRepository;

    public SppService(PembayaranSPPRepository sppRepository, UserRepository userRepository) {
        this.sppRepository = sppRepository;
        this.userRepository = userRepository;
    }

    public void processManualPayment(Long pembayaranId, Long adminUserId) {
        PembayaranSPP spp = sppRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Data pembayaran tidak ditemukan."));
        
        spp.setStatus(StatusPembayaran.LUNAS);
        spp.setTanggalBayar(LocalDate.now());
        spp.setAdmin(userRepository.getReferenceById(adminUserId));
        sppRepository.save(spp);
    }

    public void voidPayment(Long pembayaranId) {
        PembayaranSPP spp = sppRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Data pembayaran tidak ditemukan."));
        
        spp.setStatus(StatusPembayaran.BELUM_LUNAS);
        spp.setTanggalBayar(null);
        spp.setAdmin(null);
        sppRepository.save(spp);
    }
}
