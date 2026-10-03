package com.akamsi.dto;

import java.time.LocalTime;

public class TimeSlotDTO {

    private String hari;
    private int jamKe;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;

    public TimeSlotDTO() {
    }

    public TimeSlotDTO(String hari, int jamKe, LocalTime jamMulai, LocalTime jamSelesai) {
        this.hari = hari;
        this.jamKe = jamKe;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public int getJamKe() {
        return jamKe;
    }

    public void setJamKe(int jamKe) {
        this.jamKe = jamKe;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getSlotKey() {
        return this.hari + "_" + this.jamKe;
    }
}
