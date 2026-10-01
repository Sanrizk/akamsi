package com.akamsi.dto;

public class SiswaFormDTO {
    private Long id;
    private String username;
    private String password;
    private String nisn;
    private String namaSiswa;
    private String jenisKelamin;
    private String namaOrangTua;
    private String noHpOrangTua;
    private Long kelasId;

    public SiswaFormDTO() {}

    public SiswaFormDTO(Long id, String username, String password, String nisn, String namaSiswa, 
                        String jenisKelamin, String namaOrangTua, String noHpOrangTua, Long kelasId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nisn = nisn;
        this.namaSiswa = namaSiswa;
        this.jenisKelamin = jenisKelamin;
        this.namaOrangTua = namaOrangTua;
        this.noHpOrangTua = noHpOrangTua;
        this.kelasId = kelasId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    public Long getKelasId() { return kelasId; }
    public void setKelasId(Long kelasId) { this.kelasId = kelasId; }
}
