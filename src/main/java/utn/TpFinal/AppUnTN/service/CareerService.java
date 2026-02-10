package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Career;
import utn.TpFinal.AppUnTN.repository.CareerRepository;
import utn.TpFinal.AppUnTN.repository.UniversityRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CareerService {

    @Autowired
    private CareerRepository careerRepo;

    @Autowired
    private UniversityRepository universityRepo;

    public Career save(Career career) {
        if (career.getUniversity() == null || career.getUniversity().getId() == null) {
            throw new IllegalArgumentException("La carrera debe pertenecer a una universidad válida (ID requerido).");
        }

        if (!universityRepo.existsById(career.getUniversity().getId())) {
            throw new IllegalArgumentException("La universidad especificada no existe.");
        }

        if (career.getId() == null && careerRepo.existsByName(career.getName())) {
            throw new IllegalArgumentException("Ya existe una carrera con el nombre: " + career.getName());
        }

        return careerRepo.save(career);
    }

    public List<Career> getAll() {
        return careerRepo.findAll();
    }

    public Optional<Career> getById(Long id) {
        return careerRepo.findById(id);
    }

    public List<Career> findByName(String name) {
        return careerRepo.findByNameContainingIgnoreCase(name);
    }

    public void delete(Long id) {
        if (!careerRepo.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: ID no encontrado.");
        }
        careerRepo.deleteById(id);
    }
}