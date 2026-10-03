package com.akamsi.dto;

public class PresensiMingguanSummaryDTO {

    private int hadir;
    private int sakit;
    private int izin;
    private int alpa;
    private int totalPertemuan;
    private double persentaseKehadiran;

    public PresensiMingguanSummaryDTO() {
        this.hadir = 0;
        this.sakit = 0;
        this.izin = 0;
        this.alpa = 0;
        this.totalPertemuan = 0;
        this.persentaseKehadiran = 100.0;
    }

    public PresensiMingguanSummaryDTO(int hadir, int sakit, int izin, int alpa, int totalPertemuan, double persentaseKehadiran) {
        this.hadir = hadir;
        this.sakit = sakit;
        this.izin = izin;
        this.alpa = alpa;
        this.totalPertemuan = totalPertemuan;
        this.persentaseKehadiran = persentaseKehadiran;
    }

    public int getHadir() {
        return hadir;
    }

    public void setHadir(int hadir) {
        this.hadir = hadir;
    }

    public int getSakit() {
        return sakit;
    }

    public void setSakit(int sakit) {
        this.sakit = sakit;
    }

    public int getIzin() {
        return izin;
    }

    public void setIzin(int izin) {
        this.izin = izin;
    }

    public int getAlpa() {
        return alpa;
    }

    public void setAlpa(int alpa) {
        this.alpa = alpa;
    }

    public int getTotalPertemuan() {
        return totalPertemuan;
    }

    public void setTotalPertemuan(int totalPertemuan) {
        this.totalPertemuan = totalPertemuan;
    }

    public double getPersentaseKehadiran() {
        return persentaseKehadiran;
    }

    public void setPersentaseKehadiran(double persentaseKehadiran) {
        this.persentaseKehadiran = persentaseKehadiran;
    }
}
