package com.akamsi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "siswa")
public class Siswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;

    @Column(nullable = false, unique = true)
    private String nisn;

    @Column(name = "nama_siswa", nullable = false)
    private String namaSiswa;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    @Column(name = "nama_orang_tua")
    private String namaOrangTua;

    @Column(name = "no_hp_orang_tua")
    private String noHpOrangTua;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Kelas getKelas() { return kelas; }
    public void setKelas(Kelas kelas) { this.kelas = kelas; }
    
    public String getNisn() { return nisn; }
    public void setNisn(String nisn) { this.nisn = nisn; }
    
    public String getNamaSiswa() { return namaSiswa; }
    public void setNamaSiswa(String namaSiswa) { this.namaSiswa = namaSiswa; }
    
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    
    public String getNamaOrangTua() { return namaOrangTua; }
    public void setNamaOrangTua(String namaOrangTua) { this.namaOrangTua = namaOrangTua; }
    
    public String getNoHpOrangTua() { return noHpOrangTua; }
    public void setNoHpOrangTua(String noHpOrangTua) { this.noHpOrangTua = noHpOrangTua; }
}
