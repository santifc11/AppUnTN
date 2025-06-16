package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.repository.PunctuationRepository;

import java.util.List;
@Service
public class PunctuationService {

    private final PunctuationRepository punctuationRepository;

    @Autowired
    public PunctuationService(PunctuationRepository punctuationRepository) {
        this.punctuationRepository = punctuationRepository;
    }

    public Punctuation guardar(Punctuation p) {
        boolean esProfesor = p.getAuthor().getRole() == Role.PROFESSOR;
        boolean dictaMateria = p.getAuthor().getSubjects().contains(p.getDocument().getSubject());

        p.setDestacado(esProfesor && dictaMateria);

        return punctuationRepository.save(p);
    }

    public List<Punctuation> listarPorDocumentoOrdenado(Document document) {
        return punctuationRepository.findByDocumentOrderByDestacadoDesc(document);
    }

    public Punctuation actualizar(Long id, int nuevoValor) {
        if (nuevoValor < 1 || nuevoValor > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
        Punctuation puntuacion = punctuationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puntuación no encontrada"));
        puntuacion.setValue(nuevoValor);
        return punctuationRepository.save(puntuacion);
    }

    public void eliminar(Long id) {
        punctuationRepository.deleteById(id);
    }

    public double obtenerPromedioPorDocumento(Document document) {
        return punctuationRepository.findByDocumentOrderByDestacadoDesc(document).stream()
                .mapToInt(Punctuation::getValue)
                .average()
                .orElse(0.0);
    }
}

