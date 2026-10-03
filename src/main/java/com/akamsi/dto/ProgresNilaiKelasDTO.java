package com.akamsi.dto;

public class ProgresNilaiKelasDTO {

    private Long mapelId;
    private String namaMapel;
    private String namaGuru;
    private int totalSiswa;
    private int siswaDinilai;
    private int persentase;
    private String status;

    public ProgresNilaiKelasDTO() {
    }

    public ProgresNilaiKelasDTO(Long mapelId, String namaMapel, String namaGuru, int totalSiswa, int siswaDinilai, int persentase, String status) {
        this.mapelId = mapelId;
        this.namaMapel = namaMapel;
        this.namaGuru = namaGuru;
        this.totalSiswa = totalSiswa;
        this.siswaDinilai = siswaDinilai;
        this.persentase = persentase;
        this.status = status;
    }

    public Long getMapelId() {
        return mapelId;
    }

    public void setMapelId(Long mapelId) {
        this.mapelId = mapelId;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public String getNamaGuru() {
        return namaGuru;
    }

    public void setNamaGuru(String namaGuru) {
        this.namaGuru = namaGuru;
    }

    public int getTotalSiswa() {
        return totalSiswa;
    }

    public void setTotalSiswa(int totalSiswa) {
        this.totalSiswa = totalSiswa;
    }

    public int getSiswaDinilai() {
        return siswaDinilai;
    }

    public void setSiswaDinilai(int siswaDinilai) {
        this.siswaDinilai = siswaDinilai;
    }

    public int getPersentase() {
        return persentase;
    }

    public void setPersentase(int persentase) {
        this.persentase = persentase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
