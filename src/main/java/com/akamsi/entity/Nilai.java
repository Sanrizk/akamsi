package com.akamsi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nilai")
public class Nilai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    @Column(nullable = false)
    private String semester;

    @Column(name = "nilai_tugas")
    private Double nilaiTugas;

    @Column(name = "nilai_uts")
    private Double nilaiUts;

    @Column(name = "nilai_uas")
    private Double nilaiUas;

    @Column(name = "nilai_akhir")
    private Double nilaiAkhir;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    
    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public Double getNilaiTugas() { return nilaiTugas; }
    public void setNilaiTugas(Double nilaiTugas) { this.nilaiTugas = nilaiTugas; }
    
    public Double getNilaiUts() { return nilaiUts; }
    public void setNilaiUts(Double nilaiUts) { this.nilaiUts = nilaiUts; }
    
    public Double getNilaiUas() { return nilaiUas; }
    public void setNilaiUas(Double nilaiUas) { this.nilaiUas = nilaiUas; }
    
    public Double getNilaiAkhir() { return nilaiAkhir; }
    public void setNilaiAkhir(Double nilaiAkhir) { this.nilaiAkhir = nilaiAkhir; }
}
