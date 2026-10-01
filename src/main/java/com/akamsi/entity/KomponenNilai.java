package com.akamsi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "komponen_nilai")
public class KomponenNilai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kelas_id", nullable = false)
    private Kelas kelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    @Column(nullable = false)
    private String semester;

    @Column(name = "nama_penilaian", nullable = false)
    private String namaPenilaian;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KategoriNilai kategori;

    @Column(nullable = false)
    private Double bobot;

    @Column(name = "tujuan_pembelajaran", length = 500)
    private String tujuanPembelajaran;

    public KomponenNilai() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Kelas getKelas() { return kelas; }
    public void setKelas(Kelas kelas) { this.kelas = kelas; }
    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public String getNamaPenilaian() { return namaPenilaian; }
    public void setNamaPenilaian(String namaPenilaian) { this.namaPenilaian = namaPenilaian; }
    public KategoriNilai getKategori() { return kategori; }
    public void setKategori(KategoriNilai kategori) { this.kategori = kategori; }
    public Double getBobot() { return bobot; }
    public void setBobot(Double bobot) { this.bobot = bobot; }
    public String getTujuanPembelajaran() { return tujuanPembelajaran; }
    public void setTujuanPembelajaran(String tujuanPembelajaran) { this.tujuanPembelajaran = tujuanPembelajaran; }
}
