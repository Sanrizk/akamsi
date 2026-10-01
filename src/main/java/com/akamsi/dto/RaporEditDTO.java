package com.akamsi.dto;

public class RaporEditDTO {
    private Long id;
    private Long kelasId;
    private Long siswaId;
    private Integer semester;
    private String namaMapel;
    private Double nilaiAkhir;
    private String predikat;
    private String deskripsiCapaian;

    public RaporEditDTO() {}

    public RaporEditDTO(Long id, Long kelasId, Long siswaId, Integer semester, String namaMapel, Double nilaiAkhir, String predikat, String deskripsiCapaian) {
        this.id = id;
        this.kelasId = kelasId;
        this.siswaId = siswaId;
        this.semester = semester;
        this.namaMapel = namaMapel;
        this.nilaiAkhir = nilaiAkhir;
        this.predikat = predikat;
        this.deskripsiCapaian = deskripsiCapaian;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getKelasId() { return kelasId; }
    public void setKelasId(Long kelasId) { this.kelasId = kelasId; }

    public Long getSiswaId() { return siswaId; }
    public void setSiswaId(Long siswaId) { this.siswaId = siswaId; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getNamaMapel() { return namaMapel; }
    public void setNamaMapel(String namaMapel) { this.namaMapel = namaMapel; }

    public Double getNilaiAkhir() { return nilaiAkhir; }
    public void setNilaiAkhir(Double nilaiAkhir) { this.nilaiAkhir = nilaiAkhir; }

    public String getPredikat() { return predikat; }
    public void setPredikat(String predikat) { this.predikat = predikat; }

    public String getDeskripsiCapaian() { return deskripsiCapaian; }
    public void setDeskripsiCapaian(String deskripsiCapaian) { this.deskripsiCapaian = deskripsiCapaian; }
}
