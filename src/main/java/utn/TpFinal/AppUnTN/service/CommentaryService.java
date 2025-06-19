package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.repository.CommentaryRepository;

import java.util.List;

@Service
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;

    @Autowired
    public CommentaryService(CommentaryRepository commentaryRepository) {
        this.commentaryRepository = commentaryRepository;
    }

    public Commentary guardar(Commentary c) {
        boolean esProfesor = c.getAuthor().getRole() == Role.PROFESSOR;
        boolean dictaMateria = c.getAuthor().getSubjects().contains(c.getDocument().getSubject());
        c.setDestacado(esProfesor && dictaMateria);

        return commentaryRepository.save(c);
    }

    public List<Commentary> listarPorDocumento(Document document) {
        return commentaryRepository.findByDocumentOrderByDestacadoDescCreationDateDesc(document);
    }

    public void eliminar(Long id, String username) {
        Commentary comentario = commentaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        if (!comentario.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("No estás autorizado para eliminar este comentario");
        }

        commentaryRepository.deleteById(id);
    }

    public Commentary actualizar(Long id, String nuevoContenido) {
        Commentary comentario = commentaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        comentario.setContent(nuevoContenido);
        return commentaryRepository.save(comentario);
    }
}

