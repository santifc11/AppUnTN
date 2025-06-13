package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.Document;

import java.util.List;

public interface CommentaryRepository extends JpaRepository<Commentary, Long> {
    List<Commentary> findByDocument(Document document);
}