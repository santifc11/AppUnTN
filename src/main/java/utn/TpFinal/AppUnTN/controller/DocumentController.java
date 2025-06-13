package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utn.TpFinal.AppUnTN.DTO.IdRequest;
import utn.TpFinal.AppUnTN.DTO.DocumentResponseDTO;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.DocumentService;
import utn.TpFinal.AppUnTN.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final UserService userService;

    @Autowired
    public DocumentController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }

    // Agregar documento (multipart/form-data)
    @PostMapping("/add")
    public ResponseEntity<?> addDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("fileType") String fileType,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Document doc = new Document();
            doc.setTitle(title);
            doc.setDescription(description);
            doc.setFileType(fileType);
            doc.setUploadDate(LocalDate.now());
            doc.setData(file.getBytes());
            doc.setAuthor(user);

            Document savedDoc = documentService.guardar(doc);
            // Mapear a DTO para devolver solo datos necesarios
            DocumentResponseDTO dto = documentService.mapToDTO(savedDoc);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el documento: " + e.getMessage());
        }
    }

    // Eliminar documento por id, con id en RequestBody para no pasar en URL
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDocument(@RequestBody IdRequest idRequest) {
        Long id = idRequest.getId();
        Optional<Document> docOpt = documentService.buscarPorId(id);
        if (docOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado");
        }
        documentService.eliminar(id);
        return ResponseEntity.ok("Documento eliminado con éxito");
    }

    // Obtener todos los documentos en DTO
    @GetMapping("/getAll")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
        List<Document> documents = documentService.listarTodos();

        List<DocumentResponseDTO> response = documents.stream()
                .map(documentService::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Obtener documento por id en DTO
    @PostMapping("/getById")
    public ResponseEntity<?> getDocumentById(@RequestBody IdRequest idRequest) {
        Optional<Document> documentOpt = documentService.buscarPorId(idRequest.getId());
        if (documentOpt.isPresent()) {
            DocumentResponseDTO dto = documentService.mapToDTO(documentOpt.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado");
        }
    }
}
