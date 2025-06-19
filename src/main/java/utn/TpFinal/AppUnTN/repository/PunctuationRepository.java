package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;
import utn.TpFinal.AppUnTN.model.User;

import java.util.List;
import java.util.Optional;

public interface PunctuationRepository extends JpaRepository<Punctuation, Long> {
    List<Punctuation> findByDocumentOrderByDestacadoDesc(Document document);
    Optional<Punctuation> findByDocumentAndAuthor(Document document, User author);
}
