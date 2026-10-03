package com.akamsi.dto;

public class RekapPresensiKelasSummaryDTO {

    private int totalSiswa;
    private double rataRataKehadiran;
    private int siswaKritis;
    private int totalHadirSemua;
    private int totalAlpaSemua;

    public RekapPresensiKelasSummaryDTO() {
    }

    public RekapPresensiKelasSummaryDTO(int totalSiswa, double rataRataKehadiran, int siswaKritis, int totalHadirSemua, int totalAlpaSemua) {
        this.totalSiswa = totalSiswa;
        this.rataRataKehadiran = rataRataKehadiran;
        this.siswaKritis = siswaKritis;
        this.totalHadirSemua = totalHadirSemua;
        this.totalAlpaSemua = totalAlpaSemua;
    }

    public int getTotalSiswa() {
        return totalSiswa;
    }

    public void setTotalSiswa(int totalSiswa) {
        this.totalSiswa = totalSiswa;
    }

    public double getRataRataKehadiran() {
        return rataRataKehadiran;
    }

    public void setRataRataKehadiran(double rataRataKehadiran) {
        this.rataRataKehadiran = rataRataKehadiran;
    }

    public int getSiswaKritis() {
        return siswaKritis;
    }

    public void setSiswaKritis(int siswaKritis) {
        this.siswaKritis = siswaKritis;
    }

    public int getTotalHadirSemua() {
        return totalHadirSemua;
    }

    public void setTotalHadirSemua(int totalHadirSemua) {
        this.totalHadirSemua = totalHadirSemua;
    }

    public int getTotalAlpaSemua() {
        return totalAlpaSemua;
    }

    public void setTotalAlpaSemua(int totalAlpaSemua) {
        this.totalAlpaSemua = totalAlpaSemua;
    }
}
