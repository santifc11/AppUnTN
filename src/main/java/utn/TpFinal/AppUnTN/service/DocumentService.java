package utn.TpFinal.AppUnTN.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
    }

    public Document upload(Document document, User user) {
        if (user.getRole() != Role.STUDENT) {
            throw new AccessDeniedException("Solo los alumnos pueden subir documentos.");
        }
        document.setAuthor(user);
        return documentRepository.save(document);
    }
}
