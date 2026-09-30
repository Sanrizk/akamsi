package com.akamsi.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pembayaran_spp")
public class PembayaranSPP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @Column(nullable = false)
    private String bulan;

    @Column(nullable = false)
    private Integer tahun;

    @Column(name = "jumlah_bayar", nullable = false)
    private BigDecimal jumlahBayar;

    @Column(name = "tanggal_bayar")
    private LocalDate tanggalBayar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPembayaran status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    private String catatan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    
    public String getBulan() { return bulan; }
    public void setBulan(String bulan) { this.bulan = bulan; }
    
    public Integer getTahun() { return tahun; }
    public void setTahun(Integer tahun) { this.tahun = tahun; }
    
    public BigDecimal getJumlahBayar() { return jumlahBayar; }
    public void setJumlahBayar(BigDecimal jumlahBayar) { this.jumlahBayar = jumlahBayar; }
    
    public LocalDate getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(LocalDate tanggalBayar) { this.tanggalBayar = tanggalBayar; }
    
    public StatusPembayaran getStatus() { return status; }
    public void setStatus(StatusPembayaran status) { this.status = status; }
    
    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }
    
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}
