package com.akamsi.dto;

import com.akamsi.entity.StatusPembayaran;

public class LaporanSppFilterDTO {

    private Long kelasId;
    private Integer tahun;
    private String bulan;
    private StatusPembayaran status;

    public LaporanSppFilterDTO() {
    }

    public LaporanSppFilterDTO(Long kelasId, Integer tahun, String bulan, StatusPembayaran status) {
        this.kelasId = kelasId;
        this.tahun = tahun;
        this.bulan = bulan;
        this.status = status;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
    }

    public Integer getTahun() {
        return tahun;
    }

    public void setTahun(Integer tahun) {
        this.tahun = tahun;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public StatusPembayaran getStatus() {
        return status;
    }

    public void setStatus(StatusPembayaran status) {
        this.status = status;
    }
}
