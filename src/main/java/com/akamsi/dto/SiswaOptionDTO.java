package com.akamsi.dto;

public class SiswaOptionDTO {
    private Long id;
    private String nisn;
    private String namaSiswa;

    public SiswaOptionDTO() {}

    public SiswaOptionDTO(Long id, String nisn, String namaSiswa) {
        this.id = id;
        this.nisn = nisn;
        this.namaSiswa = namaSiswa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNisn() { return nisn; }
    public void setNisn(String nisn) { this.nisn = nisn; }
    
    public String getNamaSiswa() { return namaSiswa; }
    public void setNamaSiswa(String namaSiswa) { this.namaSiswa = namaSiswa; }
}

