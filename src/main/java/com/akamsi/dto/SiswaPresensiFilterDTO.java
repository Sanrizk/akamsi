package com.akamsi.dto;

import java.time.LocalDate;

public class SiswaPresensiFilterDTO {

    private Long mapelId;
    private LocalDate startDate;
    private LocalDate endDate;

    public SiswaPresensiFilterDTO() {
    }

    public SiswaPresensiFilterDTO(Long mapelId, LocalDate startDate, LocalDate endDate) {
        this.mapelId = mapelId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getMapelId() {
        return mapelId;
    }

    public void setMapelId(Long mapelId) {
        this.mapelId = mapelId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
