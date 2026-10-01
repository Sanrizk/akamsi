package com.akamsi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kurikulum")
public class Kurikulum {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_kurikulum", nullable = false)
    private String namaKurikulum;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "is_aktif", nullable = false)
    private boolean aktif;

    public Kurikulum() {}

    public Kurikulum(Long id, String namaKurikulum, String keterangan, boolean aktif) {
        this.id = id;
        this.namaKurikulum = namaKurikulum;
        this.keterangan = keterangan;
        this.aktif = aktif;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaKurikulum() { return namaKurikulum; }
    public void setNamaKurikulum(String namaKurikulum) { this.namaKurikulum = namaKurikulum; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}
