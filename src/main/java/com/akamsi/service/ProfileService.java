package com.akamsi.service;

import com.akamsi.dto.UbahPasswordDTO;
import com.akamsi.entity.Guru;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.User;
import com.akamsi.repository.GuruRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Guru> getGuruProfile(Long userId) {
        return guruRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<Siswa> getSiswaProfile(Long userId) {
        Optional<Siswa> siswaOpt = siswaRepository.findByUserId(userId);
        siswaOpt.ifPresent(siswa -> {
            if (siswa.getKelas() != null) {
                siswa.getKelas().getNamaKelas(); // initialize proxy
            }
        });
        return siswaOpt;
    }

    @Transactional
    public void ubahPassword(Long userId, UbahPasswordDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Pengguna tidak ditemukan."));

        if (!passwordEncoder.matches(dto.getPasswordLama(), user.getPassword())) {
            throw new IllegalArgumentException("Password lama yang Anda masukkan salah.");
        }

        if (dto.getPasswordBaru() == null || dto.getPasswordBaru().trim().length() < 6) {
            throw new IllegalArgumentException("Password baru minimal terdiri dari 6 karakter.");
        }

        if (!dto.getPasswordBaru().equals(dto.getKonfirmasiPassword())) {
            throw new IllegalArgumentException("Konfirmasi password baru tidak cocok.");
        }

        user.setPassword(passwordEncoder.encode(dto.getPasswordBaru().trim()));
        userRepository.save(user);
    }
}
