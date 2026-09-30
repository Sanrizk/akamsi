package com.akamsi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mata_pelajaran")
public class MataPelajaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_mapel", nullable = false)
    private String namaMapel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNamaMapel() { return namaMapel; }
    public void setNamaMapel(String namaMapel) { this.namaMapel = namaMapel; }
}
