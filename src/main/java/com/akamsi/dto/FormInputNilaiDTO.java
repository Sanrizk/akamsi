package com.akamsi.dto;

import java.util.ArrayList;
import java.util.List;

public class FormInputNilaiDTO {
    private Long komponenId;
    private Long kelasId;
    private Long mapelId;
    private List<ItemNilaiDTO> items = new ArrayList<>();

    public FormInputNilaiDTO() {}

    public Long getKomponenId() { return komponenId; }
    public void setKomponenId(Long komponenId) { this.komponenId = komponenId; }

    public Long getKelasId() { return kelasId; }
    public void setKelasId(Long kelasId) { this.kelasId = kelasId; }

    public Long getMapelId() { return mapelId; }
    public void setMapelId(Long mapelId) { this.mapelId = mapelId; }

    public List<ItemNilaiDTO> getItems() { return items; }
    public void setItems(List<ItemNilaiDTO> items) { this.items = items; }
}
