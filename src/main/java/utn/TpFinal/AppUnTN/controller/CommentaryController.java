package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.DTO.CommentaryDTO;
import utn.TpFinal.AppUnTN.DTO.CommentaryRequestDTO;
import utn.TpFinal.AppUnTN.DTO.IdRequest;
import utn.TpFinal.AppUnTN.DTO.UpdateCommentaryRequest;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.CommentaryService;
import utn.TpFinal.AppUnTN.service.DocumentService;
import utn.TpFinal.AppUnTN.service.UserService;

import java.util.List;
@RestController
@RequestMapping("/api/commentaries")
public class CommentaryController {

    private final CommentaryService commentaryService;
    private final DocumentService documentService;
    private final UserService userService;

    @Autowired
    public CommentaryController(CommentaryService commentaryService,
                                DocumentService documentService,
                                UserService userService) {
        this.commentaryService = commentaryService;
        this.documentService = documentService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<CommentaryDTO> addCommentary(@RequestBody CommentaryRequestDTO request,
                                                       Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Document document = documentService.buscarPorId(request.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        Commentary commentary = new Commentary();
        commentary.setContent(request.getContent());
        commentary.setAuthor(user);
        commentary.setDocument(document);

        Commentary saved = commentaryService.guardar(commentary);
        return ResponseEntity.ok(CommentaryDTO.fromEntity(saved));
    }

    @PostMapping("/getByDocument")
    public ResponseEntity<List<CommentaryDTO>> getByDocument(@RequestBody IdRequest request) {
        Document document = documentService.buscarPorId(request.getId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        List<CommentaryDTO> dtos = commentaryService.listarPorDocumento(document).stream()
                .map(CommentaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/update")
    public ResponseEntity<CommentaryDTO> updateCommentary(@RequestBody UpdateCommentaryRequest request) {
        Commentary updated = commentaryService.actualizar(request.getId(), request.getNuevoContenido());
        return ResponseEntity.ok(CommentaryDTO.fromEntity(updated));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteCommentary(@RequestBody IdRequest idRequest,
                                                   Authentication authentication) {
        String username = authentication.getName();
        try {
            commentaryService.eliminar(idRequest.getId(), username);
            return ResponseEntity.ok("Comentario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}


