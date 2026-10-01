package com.akamsi.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "jadwal_pelajaran")
public class JadwalPelajaran {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guru_id", nullable = false)
    private Guru guru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    @Enumerated(EnumType.STRING)
    @Column(name = "hari", nullable = false)
    private Hari hari;

    @Column(name = "jam_mulai", nullable = false)
    private LocalTime jamMulai;

    @Column(name = "jam_selesai", nullable = false)
    private LocalTime jamSelesai;

    public JadwalPelajaran() {}

    public JadwalPelajaran(Long id, Guru guru, Kelas kelas, MataPelajaran mapel, Hari hari, LocalTime jamMulai, LocalTime jamSelesai) {
        this.id = id;
        this.guru = guru;
        this.kelas = kelas;
        this.mapel = mapel;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Guru getGuru() { return guru; }
    public void setGuru(Guru guru) { this.guru = guru; }
    public Kelas getKelas() { return kelas; }
    public void setKelas(Kelas kelas) { this.kelas = kelas; }
    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
    public Hari getHari() { return hari; }
    public void setHari(Hari hari) { this.hari = hari; }
    public LocalTime getJamMulai() { return jamMulai; }
    public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }
    public LocalTime getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(LocalTime jamSelesai) { this.jamSelesai = jamSelesai; }
}
