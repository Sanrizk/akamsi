package com.akamsi.config;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import java.time.LocalTime;
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
    private final KurikulumRepository kurikulumRepository;
    private final KomponenNilaiRepository komponenNilaiRepository;
    private final NilaiSiswaRepository nilaiSiswaRepository;
    private final RaporAkhirRepository raporAkhirRepository;
    private final PembayaranSPPRepository sppRepository;
    private final JadwalPelajaranRepository jadwalPelajaranRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, GuruRepository guruRepository, 
                      KelasRepository kelasRepository, SiswaRepository siswaRepository, 
                      MataPelajaranRepository mapelRepository, KurikulumRepository kurikulumRepository,
                      KomponenNilaiRepository komponenNilaiRepository, NilaiSiswaRepository nilaiSiswaRepository,
                      RaporAkhirRepository raporAkhirRepository, PembayaranSPPRepository sppRepository, 
                      JadwalPelajaranRepository jadwalPelajaranRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.guruRepository = guruRepository;
        this.kelasRepository = kelasRepository;
        this.siswaRepository = siswaRepository;
        this.mapelRepository = mapelRepository;
        this.kurikulumRepository = kurikulumRepository;
        this.komponenNilaiRepository = komponenNilaiRepository;
        this.nilaiSiswaRepository = nilaiSiswaRepository;
        this.raporAkhirRepository = raporAkhirRepository;
        this.sppRepository = sppRepository;
        this.jadwalPelajaranRepository = jadwalPelajaranRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNamaLengkap("Administrator Utama");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

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

            Kurikulum kurmer = new Kurikulum(null, "Kurikulum Merdeka", "Fokus capaian pembelajaran (TP)", true);
            Kurikulum k13 = new Kurikulum(null, "Kurikulum 2013", "Fokus kompetensi dasar (KD)", false);
            kurikulumRepository.save(kurmer);
            kurikulumRepository.save(k13);

            Kelas kelas1A = new Kelas();
            kelas1A.setNamaKelas("Kelas 1A");
            kelas1A.setTahunAjaran("2024/2025");
            kelas1A.setWaliKelas(guru1);
            kelas1A.setKurikulum(kurmer);
            kelasRepository.save(kelas1A);

            Kelas kelas4A = new Kelas();
            kelas4A.setNamaKelas("Kelas 4A");
            kelas4A.setTahunAjaran("2024/2025");
            kelas4A.setWaliKelas(guru2);
            kelas4A.setKurikulum(kurmer);
            kelasRepository.save(kelas4A);

            String[] mapelNames = {"Matematika", "Bahasa Indonesia", "Pendidikan Pancasila"};
            for (String mapelName : mapelNames) {
                MataPelajaran mapel = new MataPelajaran();
                mapel.setNamaMapel(mapelName);
                mapelRepository.save(mapel);
            }
            List<MataPelajaran> allMapel = mapelRepository.findAll();
            MataPelajaran mtk = allMapel.get(0); 

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
                
                siswa.setKelas(i <= 2 ? kelas1A : kelas4A); 
                siswaRepository.save(siswa);
                
                PembayaranSPP spp = new PembayaranSPP();
                spp.setSiswa(siswa);
                spp.setBulan("Agustus");
                spp.setTahun(2024);
                spp.setJumlahBayar(new BigDecimal("150000"));
                spp.setStatus(StatusPembayaran.BELUM_LUNAS);
                sppRepository.save(spp);
            }

            MataPelajaran bIndo = allMapel.get(1);
            MataPelajaran pancasila = allMapel.get(2);

            JadwalPelajaran j1 = new JadwalPelajaran(null, guru1, kelas1A, mtk, Hari.SENIN, LocalTime.of(7, 30), LocalTime.of(9, 0));
            JadwalPelajaran j2 = new JadwalPelajaran(null, guru1, kelas1A, bIndo, Hari.SELASA, LocalTime.of(7, 30), LocalTime.of(9, 0));
            JadwalPelajaran j3 = new JadwalPelajaran(null, guru1, kelas4A, mtk, Hari.RABU, LocalTime.of(8, 0), LocalTime.of(9, 30));
            
            JadwalPelajaran j4 = new JadwalPelajaran(null, guru2, kelas1A, pancasila, Hari.KAMIS, LocalTime.of(7, 30), LocalTime.of(9, 0));
            JadwalPelajaran j5 = new JadwalPelajaran(null, guru2, kelas4A, pancasila, Hari.JUMAT, LocalTime.of(7, 30), LocalTime.of(8, 45));

            jadwalPelajaranRepository.save(j1);
            jadwalPelajaranRepository.save(j2);
            jadwalPelajaranRepository.save(j3);
            jadwalPelajaranRepository.save(j4);
            jadwalPelajaranRepository.save(j5);

            KomponenNilai sumatif1 = new KomponenNilai();
            sumatif1.setKelas(kelas1A);
            sumatif1.setMapel(mtk);
            sumatif1.setNamaKomponen("Sumatif Bab 1");
            sumatif1.setKategoriNilai(KategoriNilai.HARIAN);
            sumatif1.setDeskripsiTujuanPembelajaran("Mampu membaca dan menulis bilangan cacah");
            komponenNilaiRepository.save(sumatif1);

            KomponenNilai sumatif2 = new KomponenNilai();
            sumatif2.setKelas(kelas1A);
            sumatif2.setMapel(mtk);
            sumatif2.setNamaKomponen("Sumatif Bab 2");
            sumatif2.setKategoriNilai(KategoriNilai.HARIAN);
            sumatif2.setDeskripsiTujuanPembelajaran("Mampu melakukan penjumlahan dan pengurangan");
            komponenNilaiRepository.save(sumatif2);

            List<Siswa> siswaKelas1A = siswaRepository.findAll().stream()
                    .filter(s -> s.getKelas().getId().equals(kelas1A.getId()))
                    .toList();

            for (Siswa s : siswaKelas1A) {
                Double skorBab1 = 70.0 + (s.getId() * 5); 
                NilaiSiswa nilai1 = new NilaiSiswa();
                nilai1.setSiswa(s);
                nilai1.setKomponenNilai(sumatif1);
                nilai1.setNilai(skorBab1);
                nilaiSiswaRepository.save(nilai1);
                
                Double skorBab2 = 80.0 + (s.getId() * 5);
                NilaiSiswa nilai2 = new NilaiSiswa();
                nilai2.setSiswa(s);
                nilai2.setKomponenNilai(sumatif2);
                nilai2.setNilai(skorBab2);
                nilaiSiswaRepository.save(nilai2);

                Double nilaiAkhir = (skorBab1 + skorBab2) / 2; 
                RaporAkhir rapor = new RaporAkhir();
                rapor.setSiswa(s);
                rapor.setMapel(mtk);
                rapor.setSemester(1);
                rapor.setNilaiAkhir(nilaiAkhir);
                rapor.setDeskripsiCapaian("Siswa " + s.getNamaSiswa() + " menunjukkan pemahaman sangat baik pada penjumlahan dan pengurangan serta membaca bilangan cacah.");
                raporAkhirRepository.save(rapor);
            }
        }
    }
}
