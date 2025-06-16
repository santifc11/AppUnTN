package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;

import java.util.List;

public interface PunctuationRepository extends JpaRepository<Punctuation, Long> {
    List<Punctuation> findByDocumentOrderByDestacadoDesc(Document document);
}
