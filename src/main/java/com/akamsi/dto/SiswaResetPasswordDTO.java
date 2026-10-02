package com.akamsi.dto;

public class SiswaResetPasswordDTO {

    private String username;
    private String nisn;
    private String namaSiswa;
    private String namaOrangTua;
    private String noHpOrangTua;
    private Long kelasId;

    public SiswaResetPasswordDTO() {
    }

    public SiswaResetPasswordDTO(String username, String nisn, String namaSiswa, String namaOrangTua, String noHpOrangTua, Long kelasId) {
        this.username = username;
        this.nisn = nisn;
        this.namaSiswa = namaSiswa;
        this.namaOrangTua = namaOrangTua;
        this.noHpOrangTua = noHpOrangTua;
        this.kelasId = kelasId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public String getNamaOrangTua() {
        return namaOrangTua;
    }

    public void setNamaOrangTua(String namaOrangTua) {
        this.namaOrangTua = namaOrangTua;
    }

    public String getNoHpOrangTua() {
        return noHpOrangTua;
    }

    public void setNoHpOrangTua(String noHpOrangTua) {
        this.noHpOrangTua = noHpOrangTua;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
    }
}
