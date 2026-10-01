package com.akamsi.config;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GuruRepository guruRepository;
    private final KelasRepository kelasRepository;
    private final SiswaRepository siswaRepository;
    private final MataPelajaranRepository mapelRepository;
    private final PembayaranSPPRepository sppRepository;
    private final KurikulumRepository kurikulumRepository;
    private final KomponenNilaiRepository komponenNilaiRepository;
    private final NilaiSiswaRepository nilaiSiswaRepository;
    private final RaporAkhirRepository raporAkhirRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, GuruRepository guruRepository, KelasRepository kelasRepository,
                      SiswaRepository siswaRepository, MataPelajaranRepository mapelRepository,
                      PembayaranSPPRepository sppRepository, KurikulumRepository kurikulumRepository,
                      KomponenNilaiRepository komponenNilaiRepository, NilaiSiswaRepository nilaiSiswaRepository,
                      RaporAkhirRepository raporAkhirRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.guruRepository = guruRepository;
        this.kelasRepository = kelasRepository;
        this.siswaRepository = siswaRepository;
        this.mapelRepository = mapelRepository;
        this.sppRepository = sppRepository;
        this.kurikulumRepository = kurikulumRepository;
        this.komponenNilaiRepository = komponenNilaiRepository;
        this.nilaiSiswaRepository = nilaiSiswaRepository;
        this.raporAkhirRepository = raporAkhirRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed data if the database is completely empty
        if (userRepository.count() == 0) {
            
            // 1. Create Default Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNamaLengkap("Administrator Utama");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            // 2. Create 2 Teachers (Guru)
            User userGuru1 = new User();
            userGuru1.setUsername("guru1");
            userGuru1.setPassword(passwordEncoder.encode("guru123"));
            userGuru1.setNamaLengkap("Budi Santoso, S.Pd");
            userGuru1.setRole(Role.GURU);
            userRepository.save(userGuru1);

            Guru guru1 = new Guru();
            guru1.setUser(userGuru1);
            guru1.setNip("198001012005011001");
            guru1.setNoHp("081234567890");
            guruRepository.save(guru1);

            User userGuru2 = new User();
            userGuru2.setUsername("guru2");
            userGuru2.setPassword(passwordEncoder.encode("guru123"));
            userGuru2.setNamaLengkap("Siti Aminah, M.Pd");
            userGuru2.setRole(Role.GURU);
            userRepository.save(userGuru2);

            Guru guru2 = new Guru();
            guru2.setUser(userGuru2);
            guru2.setNip("198205022008012002");
            guru2.setNoHp("081987654321");
            guruRepository.save(guru2);

            // 2.5 Initialize Curricula
            Kurikulum kurmer = new Kurikulum("Kurikulum Merdeka", "Fokus capaian pembelajaran", true);
            Kurikulum k13 = new Kurikulum("Kurikulum 2013", "Fokus kompetensi", true);
            kurikulumRepository.save(kurmer);
            kurikulumRepository.save(k13);

            // 3. Create 2 Classes (Kelas)
            Kelas kelas1A = new Kelas();
            kelas1A.setNamaKelas("Kelas 1-A");
            kelas1A.setTahunAjaran("2023/2024");
            kelas1A.setWaliKelas(guru1);
            kelas1A.setKurikulum(kurmer);
            kelasRepository.save(kelas1A);

            Kelas kelas2A = new Kelas();
            kelas2A.setNamaKelas("Kelas 2-A");
            kelas2A.setTahunAjaran("2023/2024");
            kelas2A.setWaliKelas(guru2);
            kelas2A.setKurikulum(k13);
            kelasRepository.save(kelas2A);

            // 4. Create 5 Master Subjects (MataPelajaran)
            String[] mapelNames = {"Matematika", "Bahasa Indonesia", "IPA", "IPS", "Pendidikan Agama"};
            for (String mapelName : mapelNames) {
                MataPelajaran mapel = new MataPelajaran();
                mapel.setNamaMapel(mapelName);
                mapelRepository.save(mapel);
            }
            List<MataPelajaran> allMapel = mapelRepository.findAll();

            // 5. Create 4 Students (Siswa)
            for (int i = 1; i <= 4; i++) {
                User userSiswa = new User();
                userSiswa.setUsername("siswa" + i);
                userSiswa.setPassword(passwordEncoder.encode("siswa123"));
                userSiswa.setNamaLengkap("Siswa Teladan " + i);
                userSiswa.setRole(Role.SISWA);
                userRepository.save(userSiswa);

                Siswa siswa = new Siswa();
                siswa.setUser(userSiswa);
                siswa.setNisn("00100200" + i);
                siswa.setNamaSiswa(userSiswa.getNamaLengkap());
                siswa.setJenisKelamin(i % 2 == 0 ? "L" : "P");
                siswa.setNamaOrangTua("Ortu Siswa " + i);
                siswa.setNoHpOrangTua("08560000000" + i);
                // Assign first 2 students to Kelas 1-A, next 2 to Kelas 2-A
                siswa.setKelas(i <= 2 ? kelas1A : kelas2A); 
                siswaRepository.save(siswa);

                // 6. Generate Sample SPP Record for this Student
                PembayaranSPP spp = new PembayaranSPP();
                spp.setSiswa(siswa);
                spp.setBulan("Januari");
                spp.setTahun(2024);
                spp.setJumlahBayar(new BigDecimal("150000"));
                spp.setStatus(StatusPembayaran.BELUM_LUNAS);
                sppRepository.save(spp);


            }

            // 7. Generate Assessment Headers (KomponenNilai)
            KomponenNilai formatif1 = new KomponenNilai();
            formatif1.setKelas(kelas1A);
            formatif1.setMapel(allMapel.get(0));
            formatif1.setSemester("Ganjil");
            formatif1.setNamaPenilaian("Sumatif Lingkup Materi 1");
            formatif1.setKategori(KategoriNilai.FORMATIF_HARIAN);
            formatif1.setBobot(1.0);
            formatif1.setTujuanPembelajaran("Peserta didik memahami bilangan pecahan");
            komponenNilaiRepository.save(formatif1);

            // 8. Generate Student Scores (NilaiSiswa) and Final Report (RaporAkhir)
            for (Siswa s : siswaRepository.findAll()) {
                if (s.getKelas().getId().equals(kelas1A.getId())) {
                    NilaiSiswa ns = new NilaiSiswa();
                    ns.setSiswa(s);
                    ns.setKomponenNilai(formatif1);
                    ns.setNilaiAngka(85.0 + s.getId());
                    nilaiSiswaRepository.save(ns);
                    
                    RaporAkhir rapor = new RaporAkhir();
                    rapor.setSiswa(s);
                    rapor.setMapel(allMapel.get(0));
                    rapor.setSemester("Ganjil");
                    rapor.setNilaiAkhir(85.0 + s.getId());
                    rapor.setDeskripsiCapaian("Siswa menunjukkan penguasaan sangat baik.");
                    raporAkhirRepository.save(rapor);
                }
            }
        }
    }
}
