package com.akamsi.dto;

import java.math.BigDecimal;

public class LaporanSppSummaryDTO {

    private BigDecimal totalPemasukan;
    private BigDecimal totalTunggakan;
    private BigDecimal totalBatal;
    private long jumlahLunas;
    private long jumlahBelumLunas;
    private long jumlahBatal;
    private int totalData;

    public LaporanSppSummaryDTO() {
        this.totalPemasukan = BigDecimal.ZERO;
        this.totalTunggakan = BigDecimal.ZERO;
        this.totalBatal = BigDecimal.ZERO;
        this.jumlahLunas = 0;
        this.jumlahBelumLunas = 0;
        this.jumlahBatal = 0;
        this.totalData = 0;
    }

    public LaporanSppSummaryDTO(BigDecimal totalPemasukan, BigDecimal totalTunggakan, BigDecimal totalBatal,
                                long jumlahLunas, long jumlahBelumLunas, long jumlahBatal, int totalData) {
        this.totalPemasukan = totalPemasukan;
        this.totalTunggakan = totalTunggakan;
        this.totalBatal = totalBatal;
        this.jumlahLunas = jumlahLunas;
        this.jumlahBelumLunas = jumlahBelumLunas;
        this.jumlahBatal = jumlahBatal;
        this.totalData = totalData;
    }

    public BigDecimal getTotalPemasukan() {
        return totalPemasukan;
    }

    public void setTotalPemasukan(BigDecimal totalPemasukan) {
        this.totalPemasukan = totalPemasukan;
    }

    public BigDecimal getTotalTunggakan() {
        return totalTunggakan;
    }

    public void setTotalTunggakan(BigDecimal totalTunggakan) {
        this.totalTunggakan = totalTunggakan;
    }

    public BigDecimal getTotalBatal() {
        return totalBatal;
    }

    public void setTotalBatal(BigDecimal totalBatal) {
        this.totalBatal = totalBatal;
    }

    public long getJumlahLunas() {
        return jumlahLunas;
    }

    public void setJumlahLunas(long jumlahLunas) {
        this.jumlahLunas = jumlahLunas;
    }

    public long getJumlahBelumLunas() {
        return jumlahBelumLunas;
    }

    public void setJumlahBelumLunas(long jumlahBelumLunas) {
        this.jumlahBelumLunas = jumlahBelumLunas;
    }

    public long getJumlahBatal() {
        return jumlahBatal;
    }

    public void setJumlahBatal(long jumlahBatal) {
        this.jumlahBatal = jumlahBatal;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }
}
