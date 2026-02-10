package utn.TpFinal.AppUnTN.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.TpFinal.AppUnTN.model.Subject;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByNameContainingIgnoreCase(String name);

    Optional<Subject> findByName(String name);
}