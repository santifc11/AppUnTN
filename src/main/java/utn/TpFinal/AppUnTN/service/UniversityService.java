package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.TpFinal.AppUnTN.model.Career;
import utn.TpFinal.AppUnTN.model.University;
import utn.TpFinal.AppUnTN.repository.CareerRepository;
import utn.TpFinal.AppUnTN.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CareerRepository careerRepo;

    @Autowired
    private CareerService careerService;

    public University guardar(University university) {
        return universityRepository.save(university);
    }

    public List<University> listarTodas() {
        return universityRepository.findAll();
    }

    public Optional<University> buscarPorId(Long id) {
        return universityRepository.findById(id);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!universityRepository.existsById(id)) {
            throw new IllegalArgumentException("Universidad no encontrada.");
        }

        // Limpiar todas las carreras de esta universidad (y sus subjects y documentos)
        List<Career> careers = careerRepo.findByUniversityId(id);
        for (Career c : careers) {
            careerService.delete(c.getId());
        }

        universityRepository.deleteById(id);
    }


    public List<University> buscarPorNombre(String nombre) {
        return universityRepository.findByNameContainingIgnoreCase(nombre);
    }
}