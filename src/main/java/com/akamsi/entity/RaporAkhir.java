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

    @Column(nullable = false)
    private String semester;

    @Column(name = "nilai_akhir", nullable = false)
    private Double nilaiAkhir;

    @Column(name = "deskripsi_capaian", length = 1000)
    private String deskripsiCapaian;

    public RaporAkhir() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    public MataPelajaran getMapel() { return mapel; }
    public void setMapel(MataPelajaran mapel) { this.mapel = mapel; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public Double getNilaiAkhir() { return nilaiAkhir; }
    public void setNilaiAkhir(Double nilaiAkhir) { this.nilaiAkhir = nilaiAkhir; }
    public String getDeskripsiCapaian() { return deskripsiCapaian; }
    public void setDeskripsiCapaian(String deskripsiCapaian) { this.deskripsiCapaian = deskripsiCapaian; }
}
