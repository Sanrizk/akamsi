package com.akamsi.service;

import com.akamsi.dto.SiswaFormDTO;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.Role;
import com.akamsi.entity.Siswa;
import com.akamsi.entity.User;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.SiswaRepository;
import com.akamsi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SiswaService {

    private final SiswaRepository siswaRepository;
    private final UserRepository userRepository;
    private final KelasRepository kelasRepository;
    private final PasswordEncoder passwordEncoder;

    public SiswaService(SiswaRepository siswaRepository, UserRepository userRepository, 
                        KelasRepository kelasRepository, PasswordEncoder passwordEncoder) {
        this.siswaRepository = siswaRepository;
        this.userRepository = userRepository;
        this.kelasRepository = kelasRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Siswa> getAllSiswa() {
        return siswaRepository.findAll();
    }

    public Siswa getSiswaById(Long id) {
        return siswaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data Siswa tidak ditemukan."));
    }

    public void saveSiswa(SiswaFormDTO dto) {
        if (dto.getNisn() == null || dto.getNisn().trim().isEmpty()) {
            throw new RuntimeException("NISN tidak boleh kosong.");
        }
        
        String username = dto.getNisn().trim();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("NISN sudah terdaftar sebagai akun pengguna.");
        }
        
        boolean nisnExists = siswaRepository.findAll().stream()
                .anyMatch(s -> s.getNisn().equals(username));
        if (nisnExists) {
            throw new RuntimeException("NISN sudah terdaftar pada profil siswa.");
        }

        String rawPassword = (dto.getNoHpOrangTua() != null && !dto.getNoHpOrangTua().isBlank()) ? dto.getNoHpOrangTua().trim() : "siswa123";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setNamaLengkap(dto.getNamaSiswa());
        user.setRole(Role.SISWA);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Kelas kelas = kelasRepository.findById(dto.getKelasId())
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan."));

        Siswa siswa = new Siswa();
        siswa.setUser(user);
        siswa.setKelas(kelas);
        siswa.setNisn(username);
        siswa.setNamaSiswa(dto.getNamaSiswa());
        siswa.setJenisKelamin(dto.getJenisKelamin());
        siswa.setNamaOrangTua(dto.getNamaOrangTua());
        siswa.setNoHpOrangTua(dto.getNoHpOrangTua());
        siswaRepository.save(siswa);
    }

    public void updateSiswa(Long id, SiswaFormDTO dto) {
        Siswa siswa = getSiswaById(id);

        String newNisn = dto.getNisn().trim();

        boolean nisnConflict = siswaRepository.findAll().stream()
                .anyMatch(s -> s.getNisn().equals(newNisn) && !s.getId().equals(id));
        if (nisnConflict) {
            throw new RuntimeException("NISN sudah terdaftar oleh siswa lain.");
        }
        
        User user = siswa.getUser();
        boolean usernameConflict = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(newNisn) && !u.getId().equals(user.getId()));
        if (usernameConflict) {
            throw new RuntimeException("NISN/Username sudah digunakan oleh user lain.");
        }

        Kelas kelas = kelasRepository.findById(dto.getKelasId())
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan."));

        siswa.setNisn(newNisn);
        siswa.setNamaSiswa(dto.getNamaSiswa());
        siswa.setJenisKelamin(dto.getJenisKelamin());
        siswa.setNamaOrangTua(dto.getNamaOrangTua());
        siswa.setNoHpOrangTua(dto.getNoHpOrangTua());
        siswa.setKelas(kelas);

        user.setUsername(newNisn);
        user.setNamaLengkap(dto.getNamaSiswa());
        
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        userRepository.save(user);
        siswaRepository.save(siswa);
    }

    public void deleteSiswa(Long id) {
        Siswa siswa = getSiswaById(id);
        User user = siswa.getUser();
        
        siswaRepository.delete(siswa);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
