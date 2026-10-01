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

    private String keterangan;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Kurikulum() {}

    public Kurikulum(String namaKurikulum, String keterangan, boolean isActive) {
        this.namaKurikulum = namaKurikulum;
        this.keterangan = keterangan;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaKurikulum() { return namaKurikulum; }
    public void setNamaKurikulum(String namaKurikulum) { this.namaKurikulum = namaKurikulum; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
