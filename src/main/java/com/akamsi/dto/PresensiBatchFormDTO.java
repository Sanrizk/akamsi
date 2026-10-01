package com.akamsi.dto;

import com.akamsi.entity.StatusPresensi;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PresensiBatchFormDTO {
    private Long kelasId;
    private Long mapelId;
    private LocalDate tanggal;
    private List<ItemPresensiDTO> items = new ArrayList<>();

    public PresensiBatchFormDTO() {}

    public PresensiBatchFormDTO(Long kelasId, Long mapelId, LocalDate tanggal, List<ItemPresensiDTO> items) {
        this.kelasId = kelasId;
        this.mapelId = mapelId;
        this.tanggal = tanggal;
        this.items = items;
    }

    public Long getKelasId() { return kelasId; }
    public void setKelasId(Long kelasId) { this.kelasId = kelasId; }

    public Long getMapelId() { return mapelId; }
    public void setMapelId(Long mapelId) { this.mapelId = mapelId; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public List<ItemPresensiDTO> getItems() { return items; }
    public void setItems(List<ItemPresensiDTO> items) { this.items = items; }

    public static class ItemPresensiDTO {
        private Long siswaId;
        private String nisn;
        private String namaSiswa;
        private StatusPresensi status;
        private String keterangan;

        public ItemPresensiDTO() {}

        public ItemPresensiDTO(Long siswaId, String nisn, String namaSiswa, StatusPresensi status, String keterangan) {
            this.siswaId = siswaId;
            this.nisn = nisn;
            this.namaSiswa = namaSiswa;
            this.status = status;
            this.keterangan = keterangan;
        }

        public Long getSiswaId() { return siswaId; }
        public void setSiswaId(Long siswaId) { this.siswaId = siswaId; }

        public String getNisn() { return nisn; }
        public void setNisn(String nisn) { this.nisn = nisn; }

        public String getNamaSiswa() { return namaSiswa; }
        public void setNamaSiswa(String namaSiswa) { this.namaSiswa = namaSiswa; }

        public StatusPresensi getStatus() { return status; }
        public void setStatus(StatusPresensi status) { this.status = status; }

        public String getKeterangan() { return keterangan; }
        public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    }
}
