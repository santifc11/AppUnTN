package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.TpFinal.AppUnTN.model.Career;

import java.util.List;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
    boolean existsByNameIgnoreCaseAndUniversityId(String name, Long universityId);
    boolean existsByNameIgnoreCaseAndUniversityIdAndIdNot(String name, Long universityId, Long id);

    List<Career> findByNameContainingIgnoreCase(String name);
    List<Career> findByUniversityId(Long universityId);
}