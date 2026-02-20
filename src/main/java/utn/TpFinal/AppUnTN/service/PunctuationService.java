package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;
import utn.TpFinal.AppUnTN.repository.PunctuationRepository;
import utn.TpFinal.AppUnTN.repository.UserRepository;

import java.util.List;
@Service
public class PunctuationService {

    private final PunctuationRepository punctuationRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public PunctuationService(PunctuationRepository punctuationRepository,
                              UserRepository userRepository,
                              DocumentRepository documentRepository) {
        this.punctuationRepository = punctuationRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
    }

    public String guardar(Long documentId, String username, int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        var existenteOpt = punctuationRepository.findByDocumentAndAuthor(document, user);

        if (existenteOpt.isPresent()) {
            var existente = existenteOpt.get();
            existente.setValue(value);

            boolean esProfesor = user.getRole() == Role.PROFESSOR;
            boolean dictaMateria = user.getSubjects().contains(document.getSubject());

            existente.setDestacado(esProfesor && dictaMateria);

            punctuationRepository.save(existente);
            return "Puntuación actualizada correctamente.";
        }

        // Si no existe, crear nueva puntuación
        var nueva = new Punctuation();
        nueva.setAuthor(user);
        nueva.setDocument(document);
        nueva.setValue(value);

        boolean esProfesor = user.getRole() == Role.PROFESSOR;
        boolean dictaMateria = user.getSubjects().contains(document.getSubject());
        nueva.setDestacado(esProfesor && dictaMateria);

        punctuationRepository.save(nueva);
        return "Puntuación registrada correctamente.";
    }



    public List<Punctuation> listarPorDocumentoOrdenado(Document document) {
        return punctuationRepository.findByDocumentOrderByDestacadoDesc(document);
    }

    public Punctuation actualizar(Long id, int nuevoValor, String username) {
        if (nuevoValor < 1 || nuevoValor > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
        Punctuation puntuacion = punctuationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puntuación no encontrada"));

        if (!puntuacion.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("No estás autorizado para modificar esta puntuación");
        }

        puntuacion.setValue(nuevoValor);
        return punctuationRepository.save(puntuacion);
    }

    public void eliminar(Long id, String username, Role role) {
        Punctuation puntuacion = punctuationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puntuación no encontrada"));

        boolean esAutor = puntuacion.getAuthor().getUsername().equals(username);
        boolean esAdmin = role == Role.ADMIN;

        if (!esAutor && !esAdmin) {
            throw new RuntimeException("No estás autorizado para eliminar esta puntuación");
        }

        punctuationRepository.deleteById(id);
    }

    public double obtenerPromedioPorDocumento(Document document) {
        return punctuationRepository.findByDocumentOrderByDestacadoDesc(document).stream()
                .mapToInt(Punctuation::getValue)
                .average()
                .orElse(0.0);
    }
}


