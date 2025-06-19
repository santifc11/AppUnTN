package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Subject;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
   List<Document> findBySubject(Subject subject);

}
