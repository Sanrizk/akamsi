package com.akamsi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "kelas")
public class Kelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_kelas", nullable = false)
    private String namaKelas;

    @Column(name = "tahun_ajaran", nullable = false)
    private String tahunAjaran;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guru_id")
    private Guru waliKelas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }
    
    public String getTahunAjaran() { return tahunAjaran; }
    public void setTahunAjaran(String tahunAjaran) { this.tahunAjaran = tahunAjaran; }
    
    public Guru getWaliKelas() { return waliKelas; }
    public void setWaliKelas(Guru waliKelas) { this.waliKelas = waliKelas; }
}
