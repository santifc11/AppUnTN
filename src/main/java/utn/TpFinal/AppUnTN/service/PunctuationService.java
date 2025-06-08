package utn.TpFinal.AppUnTN.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;
import utn.TpFinal.AppUnTN.repository.PunctuationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PunctuationService {

    private final PunctuationRepository punctuationRepository;
    private final DocumentRepository documentRepository;

    public Punctuation rate(Long documentId, int value, User user) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        // Opcional: evitar puntuaciones duplicadas del mismo usuario
        Optional<Punctuation> existing = punctuationRepository.findAll().stream()
                .filter(p -> p.getAuthor().getId().equals(user.getId()) && p.getDocument().getId().equals(documentId))
                .findFirst();
        if (existing.isPresent()) {
            throw new RuntimeException("Ya has puntuado este documento.");
        }

        Punctuation p = new Punctuation();
        p.setDocument(document);
        p.setAuthor(user);
        p.setValue(value);

        return punctuationRepository.save(p);
    }
}
