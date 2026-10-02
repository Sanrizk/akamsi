package com.akamsi.service;

import com.akamsi.dto.SiswaResetPasswordDTO;
import com.akamsi.entity.Role;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.User;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username.trim());
    }

    @Transactional
    public boolean verifyAndResetSiswaPassword(SiswaResetPasswordDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            return false;
        }

        User user = userRepository.findByUsername(dto.getUsername().trim()).orElse(null);
        if (user == null || user.getRole() != Role.SISWA) {
            return false;
        }

        Siswa siswa = siswaRepository.findByUserId(user.getId()).orElse(null);
        if (siswa == null) {
            return false;
        }

        boolean nisnMatch = siswa.getNisn() != null && siswa.getNisn().equalsIgnoreCase(dto.getNisn().trim());
        boolean namaMatch = siswa.getNamaSiswa() != null && siswa.getNamaSiswa().equalsIgnoreCase(dto.getNamaSiswa().trim());
        boolean ortuMatch = siswa.getNamaOrangTua() != null && siswa.getNamaOrangTua().equalsIgnoreCase(dto.getNamaOrangTua().trim());

        String cleanedDbPhone = siswa.getNoHpOrangTua() != null ? siswa.getNoHpOrangTua().replaceAll("[^0-9]", "") : "";
        String cleanedInputPhone = dto.getNoHpOrangTua() != null ? dto.getNoHpOrangTua().replaceAll("[^0-9]", "") : "";
        boolean phoneMatch = !cleanedDbPhone.isEmpty() && cleanedDbPhone.equals(cleanedInputPhone);

        boolean kelasMatch = siswa.getKelas() != null && dto.getKelasId() != null && siswa.getKelas().getId().equals(dto.getKelasId());

        if (nisnMatch && namaMatch && ortuMatch && phoneMatch && kelasMatch) {
            String rawDefaultPassword = siswa.getNoHpOrangTua().trim();
            user.setPassword(passwordEncoder.encode(rawDefaultPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }
}
