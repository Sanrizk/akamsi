package com.akamsi.dto;

public class KelasAjarSummaryDTO {

    private Long kelasId;
    private String namaKelas;
    private int jumlahSiswa;
    private String daftarMapel;

    public KelasAjarSummaryDTO() {
    }

    public KelasAjarSummaryDTO(Long kelasId, String namaKelas, int jumlahSiswa, String daftarMapel) {
        this.kelasId = kelasId;
        this.namaKelas = namaKelas;
        this.jumlahSiswa = jumlahSiswa;
        this.daftarMapel = daftarMapel;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public int getJumlahSiswa() {
        return jumlahSiswa;
    }

    public void setJumlahSiswa(int jumlahSiswa) {
        this.jumlahSiswa = jumlahSiswa;
    }

    public String getDaftarMapel() {
        return daftarMapel;
    }

    public void setDaftarMapel(String daftarMapel) {
        this.daftarMapel = daftarMapel;
    }
}

