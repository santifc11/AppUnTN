package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.TpFinal.AppUnTN.model.Career;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.repository.CareerRepository;
import utn.TpFinal.AppUnTN.repository.SubjectRepository;
import utn.TpFinal.AppUnTN.repository.UniversityRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CareerService {

    @Autowired
    private CareerRepository careerRepo;

    @Autowired
    private UniversityRepository universityRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private SubjectService subjectService;

    // CareerService.java
    public Career save(Career career) {

        if (career.getUniversity() == null || career.getUniversity().getId() == null) {
            throw new IllegalArgumentException("La carrera debe estar vinculada a una universidad.");
        }

        Long uniId = career.getUniversity().getId();

        if (!universityRepo.existsById(uniId)) {
            throw new IllegalArgumentException("La universidad especificada no existe.");
        }

        if (career.getId() == null) {
            if (careerRepo.existsByNameIgnoreCaseAndUniversityId(career.getName(), uniId)) {
                throw new IllegalArgumentException("La universidad ya tiene una carrera llamada: " + career.getName());
            }
        } else {
            if (careerRepo.existsByNameIgnoreCaseAndUniversityIdAndIdNot(career.getName(), uniId, career.getId())) {
                throw new IllegalArgumentException("Ya existe otra carrera con el nombre '" + career.getName() + "' en esta universidad.");
            }
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

    @Transactional
    public void delete(Long id) {
        if (!careerRepo.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: ID no encontrado.");
        }

        // Limpiar todos los subjects de esta carrera (y sus dependencias)
        List<Subject> subjects = subjectRepo.findByCareerId(id);
        for (Subject s : subjects) {
            subjectService.delete(s.getId());
        }

        careerRepo.deleteById(id);
    }

    public List<Career> getByUniversity(Long universityId) {
        return careerRepo.findByUniversityId(universityId);
    }
}