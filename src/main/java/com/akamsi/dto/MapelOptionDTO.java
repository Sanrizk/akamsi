package com.akamsi.dto;

public class MapelOptionDTO {
    private Long id;
    private String namaMapel;

    public MapelOptionDTO() {}

    public MapelOptionDTO(Long id, String namaMapel) {
        this.id = id;
        this.namaMapel = namaMapel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaMapel() { return namaMapel; }
    public void setNamaMapel(String namaMapel) { this.namaMapel = namaMapel; }
}
