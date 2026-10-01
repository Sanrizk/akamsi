package com.akamsi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "presensi")
public class Presensi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPresensi status;

    @Column(name = "keterangan")
    private String keterangan;

    public Presensi() {}

    public Presensi(Long id, Siswa siswa, MataPelajaran mapel, LocalDate tanggal, StatusPresensi status, String keterangan) {
        this.id = id;
        this.siswa = siswa;
        this.mapel = mapel;
        this.tanggal = tanggal;
        this.status = status;
        this.keterangan = keterangan;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }

    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
    
    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    
    public StatusPresensi getStatus() { return status; }
    public void setStatus(StatusPresensi status) { this.status = status; }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}
