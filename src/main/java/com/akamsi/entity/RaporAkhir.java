package com.akamsi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rapor_akhir")
public class RaporAkhir {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapel_id", nullable = false)
    private MataPelajaran mapel;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "nilai_akhir", nullable = false)
    private Double nilaiAkhir;

    @Column(name = "deskripsi_capaian", length = 1000)
    private String deskripsiCapaian;

    public RaporAkhir() {}

    public RaporAkhir(Long id, Siswa siswa, MataPelajaran mapel, Integer semester, 
                      Double nilaiAkhir, String deskripsiCapaian) {
        this.id = id;
        this.siswa = siswa;
        this.mapel = mapel;
        this.semester = semester;
        this.nilaiAkhir = nilaiAkhir;
        this.deskripsiCapaian = deskripsiCapaian;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public Double getNilaiAkhir() { return nilaiAkhir; }
    public void setNilaiAkhir(Double nilaiAkhir) { this.nilaiAkhir = nilaiAkhir; }
    public String getDeskripsiCapaian() { return deskripsiCapaian; }
    public void setDeskripsiCapaian(String deskripsiCapaian) { this.deskripsiCapaian = deskripsiCapaian; }
}
