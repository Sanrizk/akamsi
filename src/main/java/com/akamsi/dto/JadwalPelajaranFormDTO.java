package com.akamsi.dto;

import com.akamsi.entity.Hari;

public class JadwalPelajaranFormDTO {
    private Long id;
    private Long guruId;
    private Long kelasId;
    private Long mapelId;
    private Hari hari;
    private String jamMulai;
    private String jamSelesai;

    public JadwalPelajaranFormDTO() {}

    public JadwalPelajaranFormDTO(Long id, Long guruId, Long kelasId, Long mapelId, Hari hari, String jamMulai, String jamSelesai) {
        this.id = id;
        this.guruId = guruId;
        this.kelasId = kelasId;
        this.mapelId = mapelId;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getGuruId() { return guruId; }
    public void setGuruId(Long guruId) { this.guruId = guruId; }
    
    public Long getKelasId() { return kelasId; }
    public void setKelasId(Long kelasId) { this.kelasId = kelasId; }
    
    public Long getMapelId() { return mapelId; }
    public void setMapelId(Long mapelId) { this.mapelId = mapelId; }
    
    public Hari getHari() { return hari; }
    public void setHari(Hari hari) { this.hari = hari; }
    
    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }
    
    public String getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }
}
