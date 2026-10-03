package com.akamsi.dto;

public class PresensiHariIniDTO {

    private long totalSesiDicatat;
    private long hadir;
    private long sakit;
    private long izin;
    private long alpa;
    private double persentaseKehadiran;

    public PresensiHariIniDTO() {
        this.totalSesiDicatat = 0;
        this.hadir = 0;
        this.sakit = 0;
        this.izin = 0;
        this.alpa = 0;
        this.persentaseKehadiran = 0.0;
    }

    public PresensiHariIniDTO(long totalSesiDicatat, long hadir, long sakit, long izin, long alpa, double persentaseKehadiran) {
        this.totalSesiDicatat = totalSesiDicatat;
        this.hadir = hadir;
        this.sakit = sakit;
        this.izin = izin;
        this.alpa = alpa;
        this.persentaseKehadiran = persentaseKehadiran;
    }

    public long getTotalSesiDicatat() {
        return totalSesiDicatat;
    }

    public void setTotalSesiDicatat(long totalSesiDicatat) {
        this.totalSesiDicatat = totalSesiDicatat;
    }

    public long getHadir() {
        return hadir;
    }

    public void setHadir(long hadir) {
        this.hadir = hadir;
    }

    public long getSakit() {
        return sakit;
    }

    public void setSakit(long sakit) {
        this.sakit = sakit;
    }

    public long getIzin() {
        return izin;
    }

    public void setIzin(long izin) {
        this.izin = izin;
    }

    public long getAlpa() {
        return alpa;
    }

    public void setAlpa(long alpa) {
        this.alpa = alpa;
    }

    public double getPersentaseKehadiran() {
        return persentaseKehadiran;
    }

    public void setPersentaseKehadiran(double persentaseKehadiran) {
        this.persentaseKehadiran = persentaseKehadiran;
    }
}
