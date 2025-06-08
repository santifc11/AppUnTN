package utn.TpFinal.AppUnTN.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.CommentaryRepository;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final DocumentRepository documentRepository;

    public Commentary addComment(Long documentId, String content, User user) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        Commentary commentary = new Commentary();
        commentary.setContent(content);
        commentary.setDocument(document);
        commentary.setAuthor(user);

        return commentaryRepository.save(commentary);
    }
}

