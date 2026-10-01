package com.akamsi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "komponen_nilai")
public class KomponenNilai {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_komponen", nullable = false)
    private String namaKomponen;

    @Enumerated(EnumType.STRING)
    @Column(name = "kategori_nilai", nullable = false)
    private KategoriNilai kategoriNilai;

    @Column(name = "deskripsi_tujuan_pembelajaran", length = 500)
    private String deskripsiTujuanPembelajaran;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    public KomponenNilai() {}

    public KomponenNilai(Long id, String namaKomponen, KategoriNilai kategoriNilai, 
                         String deskripsiTujuanPembelajaran, Kelas kelas, MataPelajaran mapel) {
        this.id = id;
        this.namaKomponen = namaKomponen;
        this.kategoriNilai = kategoriNilai;
        this.deskripsiTujuanPembelajaran = deskripsiTujuanPembelajaran;
        this.kelas = kelas;
        this.mapel = mapel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaKomponen() { return namaKomponen; }
    public void setNamaKomponen(String namaKomponen) { this.namaKomponen = namaKomponen; }
    public KategoriNilai getKategoriNilai() { return kategoriNilai; }
    public void setKategoriNilai(KategoriNilai kategoriNilai) { this.kategoriNilai = kategoriNilai; }
    public String getDeskripsiTujuanPembelajaran() { return deskripsiTujuanPembelajaran; }
    public void setDeskripsiTujuanPembelajaran(String deskripsiTujuanPembelajaran) { this.deskripsiTujuanPembelajaran = deskripsiTujuanPembelajaran; }
    public Kelas getKelas() { return kelas; }
    public void setKelas(Kelas kelas) { this.kelas = kelas; }
    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
}
