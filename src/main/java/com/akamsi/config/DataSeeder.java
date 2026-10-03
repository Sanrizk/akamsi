package com.akamsi.config;

import com.akamsi.entity.*;
import com.akamsi.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
    private final PresensiRepository presensiRepository;
    private final PengumumanRepository pengumumanRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, GuruRepository guruRepository, 
                      KelasRepository kelasRepository, SiswaRepository siswaRepository, 
                      MataPelajaranRepository mapelRepository, KurikulumRepository kurikulumRepository,
                      KomponenNilaiRepository komponenNilaiRepository, NilaiSiswaRepository nilaiSiswaRepository,
                      RaporAkhirRepository raporAkhirRepository, PembayaranSPPRepository sppRepository, 
                      JadwalPelajaranRepository jadwalPelajaranRepository, PresensiRepository presensiRepository, 
                      PengumumanRepository pengumumanRepository, PasswordEncoder passwordEncoder) {
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
        this.presensiRepository = presensiRepository;
        this.pengumumanRepository = pengumumanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("Database sudah terisi, membatalkan Data Seeding.");
            return;
        }

        System.out.println("Memulai Automated Data Seeding AKAMSI...");
        Random rand = new Random(42); 

        // 1. ADMIN USER
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setNamaLengkap("Adminstrator Sistem");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // 2. MASTER: Kurikulum
        Kurikulum kurmer = new Kurikulum(null, "Kurikulum Merdeka", "Fokus pada capaian pembelajaran dan profil pelajar pancasila", true);
        Kurikulum k13 = new Kurikulum(null, "Kurikulum 2013", "Fokus pada kompetensi dasar", false);
        kurikulumRepository.save(kurmer);
        kurikulumRepository.save(k13);

        // 3. MASTER: Mata Pelajaran (15)
        String[] mapelNames = {
            "Matematika", "Bahasa Indonesia", "Pendidikan Agama Islam", "Pendidikan Pancasila", 
            "Ilmu Pengetahuan Alam", "Ilmu Pengetahuan Sosial", "PJOK", "Seni Budaya", 
            "Bahasa Inggris", "Prakarya", "Bahasa Jawa", "TIK", "Sejarah", "Geografi", "Kesenian"
        };
        List<MataPelajaran> savedMapel = new ArrayList<>();
        for (String mn : mapelNames) {
            MataPelajaran mapel = new MataPelajaran();
            mapel.setNamaMapel(mn);
            savedMapel.add(mapelRepository.save(mapel));
        }

        // NAMES FOR REALISM
        String[] firstNames = {"Budi", "Andi", "Siti", "Ayu", "Joko", "Eka", "Dwi", "Tri", "Rini", "Dewi", "Wahyu", "Agung", "Putri", "Maya", "Indra", "Agus", "Ilham", "Rizky", "Fajar", "Dimas", "Bayu", "Sari", "Intan", "Ratna", "Sri", "Hendra", "Deni", "Tika", "Nia", "Rina", "Reza", "Aditya", "Nurul", "Citra", "Wulan"};
        String[] lastNames = {"Santoso", "Hidayat", "Saputra", "Pratama", "Wijaya", "Kusuma", "Lestari", "Sari", "Utami", "Setiawan", "Wibowo", "Gunawan", "Purnomo", "Nugroho", "Susanto", "Wati", "Ningsih", "Rahayu", "Handayani", "Firmansyah"};

        // 4. MASTER: Guru (20)
        List<Guru> savedGuru = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String fName = firstNames[rand.nextInt(firstNames.length)];
            String lName = lastNames[rand.nextInt(lastNames.length)];
            String degree = (i % 3 == 0) ? ", M.Pd" : ", S.Pd";
            
            User userGuru = new User();
            userGuru.setUsername("guru" + i);
            userGuru.setPassword(passwordEncoder.encode("password123"));
            userGuru.setNamaLengkap(fName + " " + lName + degree);
            userGuru.setRole(Role.GURU);
            userRepository.save(userGuru);

            Guru guru = new Guru();
            guru.setUser(userGuru);
            guru.setNip("198" + rand.nextInt(10) + "0" + (1+rand.nextInt(9)) + "15200" + (rand.nextInt(9)+1) + "01200" + i);
            guru.setNoHp("0812" + (10000000 + rand.nextInt(89999999)));
            savedGuru.add(guruRepository.save(guru));
        }

        // 5. MASTER: Kelas (12)
        List<Kelas> savedKelas = new ArrayList<>();
        String[] tingkat = {"1", "2", "3", "4", "5", "6"};
        String[] rombel = {"A", "B"};
        int guruIdx = 0;
        for (String t : tingkat) {
            for (String r : rombel) {
                Kelas kelas = new Kelas();
                kelas.setNamaKelas("Kelas " + t + r);
                kelas.setTahunAjaran("2024/2025");
                kelas.setKurikulum(Integer.parseInt(t) <= 3 ? kurmer : k13);
                kelas.setWaliKelas(savedGuru.get(guruIdx % savedGuru.size()));
                savedKelas.add(kelasRepository.save(kelas));
                guruIdx++;
            }
        }

        // 6. MASTER: Siswa (60 Siswa)
        List<Siswa> savedSiswa = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            String fName = firstNames[rand.nextInt(firstNames.length)];
            String lName = lastNames[rand.nextInt(lastNames.length)];
            
            User userSiswa = new User();
            userSiswa.setUsername("siswa" + i);
            userSiswa.setPassword(passwordEncoder.encode("password123"));
            userSiswa.setNamaLengkap(fName + " " + lName);
            userSiswa.setRole(Role.SISWA);
            userRepository.save(userSiswa);

            Siswa siswa = new Siswa();
            siswa.setUser(userSiswa);
            siswa.setNisn("00123" + (10000 + i));
            siswa.setNamaSiswa(userSiswa.getNamaLengkap());
            siswa.setJenisKelamin(rand.nextBoolean() ? "L" : "P");
            siswa.setNamaOrangTua((rand.nextBoolean() ? "Bpk. " : "Ibu ") + lastNames[rand.nextInt(lastNames.length)]);
            siswa.setNoHpOrangTua("0856" + (10000000 + rand.nextInt(89999999)));
            siswa.setKelas(savedKelas.get(i % savedKelas.size()));
            savedSiswa.add(siswaRepository.save(siswa));
        }

        // 7. TRANSAKSIONAL: Jadwal Pelajaran (2 jadwal per kelas = 24 records)
        Hari[] hariArr = {Hari.SENIN, Hari.SELASA, Hari.RABU, Hari.KAMIS, Hari.JUMAT};
        for (Kelas k : savedKelas) {
            JadwalPelajaran jp1 = new JadwalPelajaran(null, k.getWaliKelas(), k, savedMapel.get(0), Hari.SENIN, LocalTime.of(7, 30), LocalTime.of(9, 0));
            JadwalPelajaran jp2 = new JadwalPelajaran(null, savedGuru.get(rand.nextInt(savedGuru.size())), k, savedMapel.get(1), Hari.SELASA, LocalTime.of(9, 30), LocalTime.of(11, 0));
            JadwalPelajaran jp3 = new JadwalPelajaran(null, savedGuru.get(rand.nextInt(savedGuru.size())), k, savedMapel.get(2), Hari.RABU, LocalTime.of(7, 30), LocalTime.of(9, 0));
            jadwalPelajaranRepository.save(jp1);
            jadwalPelajaranRepository.save(jp2);
            jadwalPelajaranRepository.save(jp3);
        }

        // 8. TRANSAKSIONAL: Pengumuman (50 records)
        for (int i = 1; i <= 50; i++) {
            Pengumuman p = new Pengumuman();
            p.setJudul("Informasi Akademik " + i);
            p.setIsi("Ini adalah detail pengumuman ke-" + i + " mengenai kegiatan belajar mengajar, libur nasional, dan acara sekolah lainnya. Harap diperhatikan dengan saksama.");
            
            int r = rand.nextInt(3);
            p.setTargetRole(r == 0 ? "SEMUA" : (r == 1 ? "GURU" : "SISWA"));
            p.setPenulis(r == 1 ? "Kepala Sekolah" : "Adminstrator");
            p.setCreatedAt(LocalDateTime.now().minusDays(rand.nextInt(60)));
            pengumumanRepository.save(p);
        }

        // 9. TRANSAKSIONAL: Pembayaran SPP (3 bulan x 60 siswa = 180 records)
        String[] bulanSpp = {"Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        for (Siswa s : savedSiswa) {
            for (int b = 0; b < 6; b++) {
                PembayaranSPP spp = new PembayaranSPP();
                spp.setSiswa(s);
                spp.setBulan(bulanSpp[b]);
                spp.setTahun(2024);
                spp.setJumlahBayar(new BigDecimal("150000"));
                
                int prob = rand.nextInt(100);
                if (prob < 80) { // 80% lunas
                    spp.setStatus(StatusPembayaran.LUNAS);
                    spp.setTanggalBayar(LocalDate.of(2024, 7 + b, 1 + rand.nextInt(15)));
                } else if (prob < 95) { // 15% belum lunas
                    spp.setStatus(StatusPembayaran.BELUM_LUNAS);
                } else { // 5% batal
                    spp.setStatus(StatusPembayaran.BATAL);
                    spp.setCatatan("Dibatalkan sistem");
                }
                sppRepository.save(spp);
            }
        }

        // 10. TRANSAKSIONAL: Presensi (10 hari x 60 siswa = 600 records)
        LocalDate baseDate = LocalDate.now().minusDays(14);
        for (Siswa s : savedSiswa) {
            for (int d = 0; d < 10; d++) {
                Presensi p = new Presensi();
                p.setSiswa(s);
                p.setMapel(savedMapel.get(d % 3));
                p.setTanggal(baseDate.plusDays(d));
                
                int prob = rand.nextInt(100);
                if (prob < 85) p.setStatus(StatusPresensi.HADIR);
                else if (prob < 90) p.setStatus(StatusPresensi.SAKIT);
                else if (prob < 95) p.setStatus(StatusPresensi.IZIN);
                else p.setStatus(StatusPresensi.ALPA);

                p.setKeterangan(p.getStatus() != StatusPresensi.HADIR ? "Catatan otomatis" : "");
                presensiRepository.save(p);
            }
        }

        // 11. TRANSAKSIONAL: Komponen Nilai & Nilai Siswa (3 komponen x 15 mapel x 12 kelas)
        for (Kelas k : savedKelas) {
            for (int m = 0; m < 3; m++) { // Ambil 3 mapel pertama per kelas saja untuk simplifikasi processing
                MataPelajaran mapel = savedMapel.get(m);
                Guru guruMapel = k.getWaliKelas(); // Anggap wali kelas yang ajar

                // Komponen 1
                KomponenNilai kn1 = new KomponenNilai();
                kn1.setKelas(k);
                kn1.setMapel(mapel);
                kn1.setNamaKomponen("Sumatif 1 " + mapel.getNamaMapel());
                kn1.setKategoriNilai(KategoriNilai.HARIAN);
                kn1.setDeskripsiTujuanPembelajaran("Mampu memahami materi dasar");
                komponenNilaiRepository.save(kn1);

                // Komponen 2
                KomponenNilai kn2 = new KomponenNilai();
                kn2.setKelas(k);
                kn2.setMapel(mapel);
                kn2.setNamaKomponen("UTS " + mapel.getNamaMapel());
                kn2.setKategoriNilai(KategoriNilai.TENGAH_SEMESTER);
                kn2.setDeskripsiTujuanPembelajaran("Evaluasi pertengahan semester");
                komponenNilaiRepository.save(kn2);

                // Komponen 3
                KomponenNilai kn3 = new KomponenNilai();
                kn3.setKelas(k);
                kn3.setMapel(mapel);
                kn3.setNamaKomponen("UAS " + mapel.getNamaMapel());
                kn3.setKategoriNilai(KategoriNilai.AKHIR_SEMESTER);
                kn3.setDeskripsiTujuanPembelajaran("Evaluasi akhir semester");
                komponenNilaiRepository.save(kn3);

                // Buat Nilai Siswa dan Rapor Akhir untuk semua siswa di kelas ini
                for (Siswa s : savedSiswa) {
                    if (!s.getKelas().getId().equals(k.getId())) continue;

                    Double n1 = 60.0 + rand.nextInt(40);
                    Double n2 = 60.0 + rand.nextInt(40);
                    Double n3 = 60.0 + rand.nextInt(40);

                    NilaiSiswa ns1 = new NilaiSiswa();
                    ns1.setSiswa(s);
                    ns1.setKomponenNilai(kn1);
                    ns1.setNilai(n1);
                    nilaiSiswaRepository.save(ns1);

                    NilaiSiswa ns2 = new NilaiSiswa();
                    ns2.setSiswa(s);
                    ns2.setKomponenNilai(kn2);
                    ns2.setNilai(n2);
                    nilaiSiswaRepository.save(ns2);

                    NilaiSiswa ns3 = new NilaiSiswa();
                    ns3.setSiswa(s);
                    ns3.setKomponenNilai(kn3);
                    ns3.setNilai(n3);
                    nilaiSiswaRepository.save(ns3);

                    Double nilaiAkhir = (n1 * 0.3) + (n2 * 0.3) + (n3 * 0.4);
                    String predikat = nilaiAkhir >= 90 ? "A" : (nilaiAkhir >= 80 ? "B" : (nilaiAkhir >= 70 ? "C" : "D"));

                    RaporAkhir rapor = new RaporAkhir();
                    rapor.setSiswa(s);
                    rapor.setMapel(mapel);
                    rapor.setNamaKelas(k.getNamaKelas());
                    rapor.setSemester(1);
                    rapor.setNilaiAkhir(Math.round(nilaiAkhir * 100.0) / 100.0);
                    rapor.setPredikat(predikat);
                    rapor.setDeskripsiCapaian("Siswa menunjukkan capaian " + predikat + " dalam pembelajaran materi ini dengan baik.");
                    raporAkhirRepository.save(rapor);
                }
            }
        }

        System.out.println("Automated Data Seeding AKAMSI selesai! Lebih dari ribuan record berhasil digenerate.");
    }
}
