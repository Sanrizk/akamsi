package com.akamsi.controller;

import com.akamsi.entity.JadwalPelajaran;
import com.akamsi.entity.Kelas;
import com.akamsi.repository.JadwalPelajaranRepository;
import com.akamsi.service.AkademikService;
import com.akamsi.service.JadwalGeneratorService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@WebMvcTest(
    controllers = AdminJadwalController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class AdminJadwalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JadwalPelajaranRepository jadwalPelajaranRepository;

    @MockBean
    private AkademikService akademikService;

    @MockBean
    private JadwalGeneratorService jadwalGeneratorService;

    @Test
    public void testIndex_WithAdminRole_ShouldReturnJadwalView() throws Exception {
        Kelas kelas = new Kelas();
        kelas.setId(1L);
        kelas.setNamaKelas("Kelas 1A");

        Mockito.when(akademikService.getAllKelas()).thenReturn(List.of(kelas));
        Mockito.when(akademikService.findKelasById(1L)).thenReturn(kelas);
        Mockito.when(jadwalPelajaranRepository.findByKelasIdOrderByHariAscJamMulaiAsc(1L)).thenReturn(new ArrayList<>());
        Mockito.when(jadwalPelajaranRepository.count()).thenReturn(10L);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/jadwal"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/jadwal"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("kelasList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("selectedKelasId"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("jadwalList"))
                .andExpect(MockMvcResultMatchers.model().attribute("totalJadwalSemua", 10L));
    }

    @Test
    public void testGenerateOtomatis_Success_ShouldRedirectWithFlashAttribute() throws Exception {
        Mockito.when(jadwalGeneratorService.generateJadwalOtomatis(true)).thenReturn(40);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/jadwal/generate")
                .param("resetJadwalLama", "true"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/jadwal"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("success"));
    }

    @Test
    public void testGenerateOtomatis_Failure_ShouldRedirectWithErrorFlashAttribute() throws Exception {
        Mockito.when(jadwalGeneratorService.generateJadwalOtomatis(true))
               .thenThrow(new IllegalStateException("Prasyarat belum terpenuhi"));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/jadwal/generate")
                .param("resetJadwalLama", "true"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/jadwal"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"));
    }
}

