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

    @Column(name = "nilai", nullable = false)
    private Double nilai;

    public NilaiSiswa() {}

    public NilaiSiswa(Long id, Siswa siswa, KomponenNilai komponenNilai, Double nilai) {
        this.id = id;
        this.siswa = siswa;
        this.komponenNilai = komponenNilai;
        this.nilai = nilai;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    public KomponenNilai getKomponenNilai() { return komponenNilai; }
    public void setKomponenNilai(KomponenNilai komponenNilai) { this.komponenNilai = komponenNilai; }
    public Double getNilai() { return nilai; }
    public void setNilai(Double nilai) { this.nilai = nilai; }
}
