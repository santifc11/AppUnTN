package utn.TpFinal.AppUnTN.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.repository.CareerRepository;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;
import utn.TpFinal.AppUnTN.repository.SubjectRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private CareerRepository careerRepo;

    @Autowired
    private DocumentRepository documentRepo;


    public Subject save(Subject subject) {

        if (subject.getCareer() == null || subject.getCareer().getId() == null) {
            throw new IllegalArgumentException("La materia debe pertenecer a una carrera válida (ID requerido).");
        }
        Long careerId = subject.getCareer().getId();
        if (!careerRepo.existsById(careerId)) {
            throw new IllegalArgumentException("La carrera especificada no existe.");
        }

        if (subject.getId() == null) {
            if (subjectRepo.existsByNameIgnoreCaseAndCareerId(subject.getName(), careerId)) {
                throw new IllegalArgumentException("Ya existe una materia llamada '" + subject.getName() + "' en esta carrera.");
            }
        } else {
            if (subjectRepo.existsByNameIgnoreCaseAndCareerIdAndIdNot(subject.getName(), careerId, subject.getId())) {
                throw new IllegalArgumentException("El nombre '" + subject.getName() + "' ya está siendo usado por otra materia en esta misma carrera.");
            }
        }

        return subjectRepo.save(subject);
    }

    public List<Subject> getAll() {
        return subjectRepo.findAll();
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepo.findById(id);
    }

    public List<Subject> findByName(String name) {
        return subjectRepo.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public void delete(Long id) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se puede eliminar: ID no encontrado."));

        // 1. Limpiar la tabla user_subjects (FK de profesores)
        subjectRepo.removeFromAllUsers(id);

        // 2. Borrar todos los documentos de esta materia (cascade borra commentaries + punctuations)
        List<Document> docs = documentRepo.findBySubject(subject);
        documentRepo.deleteAll(docs);

        // 3. Borrar la materia
        subjectRepo.deleteById(id);
    }

    public List<Subject> getByCareer(Long careerId) {
        return subjectRepo.findByCareerId(careerId);
    }

}
