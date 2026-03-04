package utn.TpFinal.AppUnTN.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.TpFinal.AppUnTN.model.Subject;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByNameContainingIgnoreCase(String name);
    List<Subject> findByCareerId(Long careerId);
    Optional<Subject> findByName(String name);

    boolean existsByNameIgnoreCaseAndCareerId(String name, Long careerId);

    boolean existsByNameIgnoreCaseAndCareerIdAndIdNot(String name, Long careerId, Long id);

    @Modifying
    @Query(value = "DELETE FROM user_subjects WHERE subject_id = :subjectId", nativeQuery = true)
    void removeFromAllUsers(@Param("subjectId") Long subjectId);
}