package com.akamsi.service;

import com.akamsi.entity.Kelas;
import com.akamsi.entity.MataPelajaran;
import com.akamsi.repository.KelasRepository;
import com.akamsi.repository.MataPelajaranRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AkademikService {
    private final KelasRepository kelasRepository;
    private final MataPelajaranRepository mapelRepository;

    public AkademikService(KelasRepository kelasRepository, MataPelajaranRepository mapelRepository) {
        this.kelasRepository = kelasRepository;
        this.mapelRepository = mapelRepository;
    }

    public List<Kelas> getAllKelas() { 
        return kelasRepository.findAll(); 
    }
    
    public void saveKelas(Kelas kelas) { 
        kelasRepository.save(kelas); 
    }
    
    public void deleteKelas(Long id) { 
        kelasRepository.deleteById(id); 
    }

    public List<MataPelajaran> getAllMapel() { 
        return mapelRepository.findAll(); 
    }
    
    public void saveMapel(MataPelajaran mapel) { 
        mapelRepository.save(mapel); 
    }
    
    public void deleteMapel(Long id) { 
        mapelRepository.deleteById(id); 
    }
}
