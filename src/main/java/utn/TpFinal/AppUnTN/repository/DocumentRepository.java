package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.model.User;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
   List<Document> findBySubject(Subject subject);
   void deleteAllByAuthor(User author);
   List<Document> findByAuthor(User author);

}
