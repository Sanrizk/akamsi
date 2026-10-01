package com.akamsi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nilai_siswa")
public class NilaiSiswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "komponen_nilai_id", nullable = false)
    private KomponenNilai komponenNilai;

    @Column(name = "nilai_angka", nullable = false)
    private Double nilaiAngka;

    public NilaiSiswa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    public KomponenNilai getKomponenNilai() { return komponenNilai; }
    public void setKomponenNilai(KomponenNilai komponenNilai) { this.komponenNilai = komponenNilai; }
    public Double getNilaiAngka() { return nilaiAngka; }
    public void setNilaiAngka(Double nilaiAngka) { this.nilaiAngka = nilaiAngka; }
}
