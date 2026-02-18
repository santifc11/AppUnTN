package utn.TpFinal.AppUnTN.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utn.TpFinal.AppUnTN.DTO.*;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.DocumentService;
import utn.TpFinal.AppUnTN.service.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
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

    // Agregar documento
    @PostMapping("/add")
    public ResponseEntity<?> addDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("subject") String subject,
            @RequestParam("fileType") String fileType,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Document doc = new Document();
            doc.setTitle(title);
            doc.setDescription(description);
            doc.setSubject(Subject.valueOf(subject));
            doc.setFileType(fileType);
            doc.setUploadDate(LocalDate.now());
            doc.setData(file.getBytes());
            doc.setAuthor(user);

            Document savedDoc = documentService.guardar(doc);

            DocumentResponseDTO dto = documentService.mapToDTO(savedDoc);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el documento: " + e.getMessage());
        }
    }

    // Eliminar documento por id, con id en RequestBody para no pasar en URL, administradores o autores lo pueden borrar
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDocument(@RequestBody IdRequest idRequest, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long id = idRequest.getId();
        Optional<Document> docOpt = documentService.buscarPorId(id);

        if (docOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado");
        }

        Document doc = docOpt.get();

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isAuthor = doc.getAuthor().getId().equals(user.getId());

        if (!(isAdmin || isAuthor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tenés permisos para eliminar este documento.");
        }

        documentService.eliminar(id);
        return ResponseEntity.ok("Documento eliminado con éxito");
    }


    // Obtener todos los documentos en DTO
    @GetMapping("/getAll")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments(
            @RequestParam(defaultValue = "recientes") String orden) {
        List<Document> documents = documentService.listarTodos();

        List<DocumentResponseDTO> response = documents.stream()
                .map(documentService::mapToDTO)
                .collect(Collectors.toList());

        switch (orden) {
            case "puntuados":
                response.sort((a, b) -> {
                    double promedioA = a.getPunctuations().isEmpty() ? 0 :
                            a.getPunctuations().stream().mapToInt(DocumentResponseDTO.PunctuationDTO::getValue).average().orElse(0);
                    double promedioB = b.getPunctuations().isEmpty() ? 0 :
                            b.getPunctuations().stream().mapToInt(DocumentResponseDTO.PunctuationDTO::getValue).average().orElse(0);
                    return Double.compare(promedioB, promedioA);
                });
                break;
            case "descargados":
                response.sort(Comparator.comparingInt(DocumentResponseDTO::getDownloadCount).reversed());
                break;
            default: // "recientes"
                response.sort(Comparator.comparing(DocumentResponseDTO::getUploadDate).reversed());
                break;
        }

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



    @PostMapping("/filterBySubject")
    public ResponseEntity<List<DocumentResponseDTO>> filterBySubject(@RequestBody FilterSubjectDTO filter) {
        try {
            Subject subject = Subject.valueOf(filter.getSubject().toUpperCase());
            List<Document> documents = documentService.findBySubject(subject);

            List<DocumentResponseDTO> response = documents.stream()
                    .map(documentService::mapToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFileById(@RequestBody IdRequest idRequest) {
        return documentService.buscarPorId(idRequest.getId())
                .map(document -> {
                    documentService.incrementarDescargas(document);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(document.getFileType()));
                    headers.setContentDisposition(ContentDisposition.attachment()
                            .filename(document.getTitle() + "." + document.getFileType())
                            .build());
                    return new ResponseEntity<>(document.getData(), headers, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDocument(
            @RequestBody @Valid DocumentUpdateDTO documentUpdateDTO,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tenés permisos para modificar documentos.");
        }

        Optional<Document> existingOpt = documentService.buscarPorId(documentUpdateDTO.getId());
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado");
        }

        Document existing = existingOpt.get();

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isAuthor = existing.getAuthor().getId().equals(user.getId());

        if (!(isAdmin || isAuthor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tenés permisos para modificar este documento.");
        }

        existing.setTitle(documentUpdateDTO.getTitle());
        existing.setDescription(documentUpdateDTO.getDescription());
        existing.setSubject(documentUpdateDTO.getSubject());

        Document saved = documentService.guardar(existing);

        return ResponseEntity.ok(documentService.mapToDTO(saved));
    }

    @GetMapping("/myDocuments")
    public ResponseEntity<List<DocumentResponseDTO>> getMyDocuments(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Document> docs = documentService.findByAuthor(user);

        List<DocumentResponseDTO> response = docs.stream()
                .map(documentService::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }



}
