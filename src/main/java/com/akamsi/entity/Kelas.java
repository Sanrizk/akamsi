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
@Table(name = "kelas")
public class Kelas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_kelas", nullable = false, unique = true)
    private String namaKelas;

    @Column(name = "tingkat")
    private Integer tingkat;

    @Column(name = "tahun_ajaran")
    private String tahunAjaran;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wali_kelas_id")
    private Guru waliKelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurikulum_id")
    private Kurikulum kurikulum;

    public Kelas() {
    }

    public Kelas(Long id, String namaKelas, Integer tingkat, String tahunAjaran, Guru waliKelas, Kurikulum kurikulum) {
        this.id = id;
        this.namaKelas = namaKelas;
        this.tingkat = tingkat;
        this.tahunAjaran = tahunAjaran;
        this.waliKelas = waliKelas;
        this.kurikulum = kurikulum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public Integer getTingkat() {
        return tingkat;
    }

    public void setTingkat(Integer tingkat) {
        this.tingkat = tingkat;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public Guru getWaliKelas() {
        return waliKelas;
    }

    public void setWaliKelas(Guru waliKelas) {
        this.waliKelas = waliKelas;
    }

    public Kurikulum getKurikulum() {
        return kurikulum;
    }

    public void setKurikulum(Kurikulum kurikulum) {
        this.kurikulum = kurikulum;
    }
}
