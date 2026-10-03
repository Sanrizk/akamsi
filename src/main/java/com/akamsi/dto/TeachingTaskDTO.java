package com.akamsi.dto;

import com.akamsi.entity.Guru;
import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;

public class TeachingTaskDTO {

    private Kelas kelas;
    private MataPelajaran mapel;
    private Guru guru;
    private int durasiJp;

    public TeachingTaskDTO() {
    }

    public TeachingTaskDTO(Kelas kelas, MataPelajaran mapel, Guru guru, int durasiJp) {
        this.kelas = kelas;
        this.mapel = mapel;
        this.guru = guru;
        this.durasiJp = durasiJp;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public MataPelajaran getMapel() {
        return mapel;
    }

    public void setMapel(MataPelajaran mapel) {
        this.mapel = mapel;
    }

    public Guru getGuru() {
        return guru;
    }

    public void setGuru(Guru guru) {
        this.guru = guru;
    }

    public int getDurasiJp() {
        return durasiJp;
    }

    public void setDurasiJp(int durasiJp) {
        this.durasiJp = durasiJp;
    }
}
