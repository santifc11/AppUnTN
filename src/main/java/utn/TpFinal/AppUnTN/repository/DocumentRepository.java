package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {}
