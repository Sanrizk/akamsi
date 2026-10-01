package com.akamsi.dto;

public class ItemNilaiDTO {
    private Long siswaId;
    private String nisn;
    private String namaSiswa;
    private Double nilai;

    public ItemNilaiDTO() {}

    public Long getSiswaId() { return siswaId; }
    public void setSiswaId(Long siswaId) { this.siswaId = siswaId; }

    public String getNisn() { return nisn; }
    public void setNisn(String nisn) { this.nisn = nisn; }

    public String getNamaSiswa() { return namaSiswa; }
    public void setNamaSiswa(String namaSiswa) { this.namaSiswa = namaSiswa; }

    public Double getNilai() { return nilai; }
    public void setNilai(Double nilai) { this.nilai = nilai; }
}
