package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.CommentaryRepository;
import utn.TpFinal.AppUnTN.repository.UserRepository;

import java.util.List;

@Service
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentaryService(CommentaryRepository commentaryRepository, UserRepository userRepository) {
        this.commentaryRepository = commentaryRepository;
        this.userRepository = userRepository;
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

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAuthor = comentario.getAuthor().getUsername().equals(username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!(isAuthor || isAdmin)) {
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

