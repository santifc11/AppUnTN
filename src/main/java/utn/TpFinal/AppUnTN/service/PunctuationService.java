package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;
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
        return punctuationRepository.save(p);
    }

    public List<Punctuation> listarPorDocumento(Document document) {
        return punctuationRepository.findByDocument(document);
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
        return punctuationRepository.findByDocument(document).stream()
                .mapToInt(Punctuation::getValue)
                .average()
                .orElse(0.0);
    }
}

