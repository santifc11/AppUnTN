package utn.TpFinal.AppUnTN.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
}