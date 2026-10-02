package com.akamsi.service;

import com.akamsi.entity.PembayaranSPP;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.StatusPembayaran;
import com.akamsi.entity.User;
import com.akamsi.repository.PembayaranSPPRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SppService {

    public static final BigDecimal DEFAULT_NOMINAL_SPP = new BigDecimal("150000");

    @Autowired
    private PembayaranSPPRepository pembayaranSPPRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private UserRepository userRepository;

    private static final List<String> DAFTAR_BULAN = Arrays.asList(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    );

    @Transactional
    public int generateTagihanSpp(Long kelasId, Integer tahun, BigDecimal nominal) {
        BigDecimal nominalFinal = (nominal != null && nominal.compareTo(BigDecimal.ZERO) > 0)
                ? nominal
                : DEFAULT_NOMINAL_SPP;

        List<Siswa> targetSiswa;
        if (kelasId != null) {
            targetSiswa = siswaRepository.findByKelasId(kelasId);
        } else {
            targetSiswa = siswaRepository.findAll();
        }

        List<PembayaranSPP> newBills = new ArrayList<>();
        int countCreated = 0;

        for (Siswa siswa : targetSiswa) {
            for (String bulan : DAFTAR_BULAN) {
                boolean alreadyExists = pembayaranSPPRepository
                        .existsBySiswaIdAndBulanAndTahun(siswa.getId(), bulan, tahun);

                if (!alreadyExists) {
                    PembayaranSPP spp = new PembayaranSPP();
                    spp.setSiswa(siswa);
                    spp.setBulan(bulan);
                    spp.setTahun(tahun);
                    spp.setJumlahBayar(nominalFinal);
                    spp.setStatus(StatusPembayaran.BELUM_LUNAS);
                    spp.setTanggalBayar(null);
                    spp.setAdmin(null);
                    newBills.add(spp);
                    countCreated++;
                }
            }
        }

        if (!newBills.isEmpty()) {
            pembayaranSPPRepository.saveAll(newBills);
        }

        return countCreated;
    }

    @Transactional
    public void processManualPayment(Long pembayaranId, Long adminUserId) {
        PembayaranSPP spp = pembayaranSPPRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Tagihan SPP tidak ditemukan dengan ID: " + pembayaranId));

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan dengan ID: " + adminUserId));

        spp.setStatus(StatusPembayaran.LUNAS);
        spp.setTanggalBayar(LocalDate.now());
        spp.setAdmin(admin);
        pembayaranSPPRepository.save(spp);
    }

    @Transactional
    public void voidPayment(Long pembayaranId) {
        PembayaranSPP spp = pembayaranSPPRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Tagihan SPP tidak ditemukan dengan ID: " + pembayaranId));

        spp.setStatus(StatusPembayaran.BELUM_LUNAS);
        spp.setTanggalBayar(null);
        spp.setAdmin(null);
        pembayaranSPPRepository.save(spp);
    }

    @Transactional
    public void nonaktifkanTagihan(Long pembayaranId) {
        PembayaranSPP spp = pembayaranSPPRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Tagihan SPP tidak ditemukan dengan ID: " + pembayaranId));

        if (spp.getStatus() == StatusPembayaran.LUNAS) {
            throw new IllegalStateException("Tagihan yang sudah LUNAS tidak dapat dinonaktifkan secara langsung. Batalkan pembayaran terlebih dahulu.");
        }

        spp.setStatus(StatusPembayaran.BATAL);
        spp.setTanggalBayar(null);
        spp.setAdmin(null);
        pembayaranSPPRepository.save(spp);
    }

    @Transactional
    public void aktifkanTagihan(Long pembayaranId) {
        PembayaranSPP spp = pembayaranSPPRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Tagihan SPP tidak ditemukan dengan ID: " + pembayaranId));

        spp.setStatus(StatusPembayaran.BELUM_LUNAS);
        pembayaranSPPRepository.save(spp);
    }

    @Transactional
    public void hapusTagihan(Long pembayaranId) {
        PembayaranSPP spp = pembayaranSPPRepository.findById(pembayaranId)
                .orElseThrow(() -> new RuntimeException("Tagihan SPP tidak ditemukan dengan ID: " + pembayaranId));

        if (spp.getStatus() == StatusPembayaran.LUNAS) {
            throw new IllegalStateException("Tagihan yang berstatus LUNAS tidak boleh dihapus demi keamanan riwayat keuangan.");
        }

        pembayaranSPPRepository.delete(spp);
    }
}
