package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.model.User;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
   List<Document> findBySubject(Subject subject);
   void deleteAllByAuthor(User author);
   List<Document> findByAuthor(User author);

   @Query("SELECT DISTINCT d FROM Document d " +
          "LEFT JOIN FETCH d.author " +
          "LEFT JOIN FETCH d.punctuations p " +
          "LEFT JOIN FETCH p.author " +
          "LEFT JOIN FETCH d.commentaries c " +
          "LEFT JOIN FETCH c.author")
   List<Document> findAllWithDetails();

   @Query("SELECT DISTINCT d FROM Document d " +
          "LEFT JOIN FETCH d.author " +
          "LEFT JOIN FETCH d.punctuations p " +
          "LEFT JOIN FETCH p.author " +
          "LEFT JOIN FETCH d.commentaries c " +
          "LEFT JOIN FETCH c.author " +
          "WHERE d.id = :id")
   Optional<Document> findByIdWithDetails(@Param("id") Long id);

   @Query("SELECT DISTINCT d FROM Document d " +
          "LEFT JOIN FETCH d.author " +
          "LEFT JOIN FETCH d.punctuations p " +
          "LEFT JOIN FETCH p.author " +
          "LEFT JOIN FETCH d.commentaries c " +
          "LEFT JOIN FETCH c.author " +
          "WHERE d.subject = :subject")
   List<Document> findBySubjectWithDetails(@Param("subject") Subject subject);

   @Query("SELECT DISTINCT d FROM Document d " +
          "LEFT JOIN FETCH d.author " +
          "LEFT JOIN FETCH d.punctuations p " +
          "LEFT JOIN FETCH p.author " +
          "LEFT JOIN FETCH d.commentaries c " +
          "LEFT JOIN FETCH c.author " +
          "WHERE d.author = :author")
   List<Document> findByAuthorWithDetails(@Param("author") User author);

}
