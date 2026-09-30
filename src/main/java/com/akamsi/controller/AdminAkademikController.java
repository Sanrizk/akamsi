package com.akamsi.controller;

import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.service.AkademikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/akademik")
public class AdminAkademikController {
    private final AkademikService akademikService;

    public AdminAkademikController(AkademikService akademikService) {
        this.akademikService = akademikService;
    }

    @GetMapping("/kelas")
    public String listKelas(Model model) {
        model.addAttribute("kelasList", akademikService.getAllKelas());
        return "admin/akademik/kelas"; 
    }

    @PostMapping("/kelas/add")
    public String addKelas(@ModelAttribute Kelas kelas) {
        akademikService.saveKelas(kelas);
        return "redirect:/admin/akademik/kelas";
    }

    @GetMapping("/mapel")
    public String listMapel(Model model) {
        model.addAttribute("mapelList", akademikService.getAllMapel());
        return "admin/akademik/mapel";
    }

    @PostMapping("/mapel/add")
    public String addMapel(@ModelAttribute MataPelajaran mapel) {
        akademikService.saveMapel(mapel);
        return "redirect:/admin/akademik/mapel";
    }
}
