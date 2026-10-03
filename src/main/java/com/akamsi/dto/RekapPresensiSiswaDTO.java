package com.akamsi.dto;

public class RekapPresensiSiswaDTO {

    private Long siswaId;
    private String nisn;
    private String namaSiswa;
    private int hadir;
    private int sakit;
    private int izin;
    private int alpa;
    private int totalPertemuan;
    private double persentaseKehadiran;

    public RekapPresensiSiswaDTO() {
    }

    public RekapPresensiSiswaDTO(Long siswaId, String nisn, String namaSiswa, int hadir, int sakit, int izin, int alpa, int totalPertemuan, double persentaseKehadiran) {
        this.siswaId = siswaId;
        this.nisn = nisn;
        this.namaSiswa = namaSiswa;
        this.hadir = hadir;
        this.sakit = sakit;
        this.izin = izin;
        this.alpa = alpa;
        this.totalPertemuan = totalPertemuan;
        this.persentaseKehadiran = persentaseKehadiran;
    }

    public Long getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(Long siswaId) {
        this.siswaId = siswaId;
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
