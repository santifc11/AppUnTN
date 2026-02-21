package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.University;
import utn.TpFinal.AppUnTN.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public University guardar(University university) {
        return universityRepository.save(university);
    }

    public List<University> listarTodas() {
        return universityRepository.findAll();
    }

    public Optional<University> buscarPorId(Long id) {
        return universityRepository.findById(id);
    }

    public void eliminar(Long id) {
        universityRepository.deleteById(id);
    }


    public List<University> buscarPorNombre(String nombre) {
        return universityRepository.findByNameContainingIgnoreCase(nombre);
    }
}