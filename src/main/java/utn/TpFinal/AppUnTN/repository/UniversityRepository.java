package utn.TpFinal.AppUnTN.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.University;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findByNameContainingIgnoreCase(String name);

}