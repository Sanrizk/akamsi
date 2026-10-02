package com.akamsi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rapor_akhir")
public class RaporAkhir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @Column(name = "nama_kelas", nullable = false)
    private String namaKelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "nilai_akhir")
    private Double nilaiAkhir;

    @Column(name = "predikat")
    private String predikat;

    @Column(name = "deskripsi_capaian", columnDefinition = "TEXT")
    private String deskripsiCapaian;

    public RaporAkhir() {
    }

    public RaporAkhir(Long id, Siswa siswa, String namaKelas, MataPelajaran mapel, Integer semester, Double nilaiAkhir, String predikat, String deskripsiCapaian) {
        this.id = id;
        this.siswa = siswa;
        this.namaKelas = namaKelas;
        this.mapel = mapel;
        this.semester = semester;
        this.nilaiAkhir = nilaiAkhir;
        this.predikat = predikat;
        this.deskripsiCapaian = deskripsiCapaian;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public MataPelajaran getMapel() {
        return mapel;
    }

    public void setMapel(MataPelajaran mapel) {
        this.mapel = mapel;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Double getNilaiAkhir() {
        return nilaiAkhir;
    }

    public void setNilaiAkhir(Double nilaiAkhir) {
        this.nilaiAkhir = nilaiAkhir;
    }

    public String getPredikat() {
        return predikat;
    }

    public void setPredikat(String predikat) {
        this.predikat = predikat;
    }

    public String getDeskripsiCapaian() {
        return deskripsiCapaian;
    }

    public void setDeskripsiCapaian(String deskripsiCapaian) {
        this.deskripsiCapaian = deskripsiCapaian;
    }
}
