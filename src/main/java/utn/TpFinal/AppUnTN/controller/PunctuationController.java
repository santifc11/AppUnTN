package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.DTO.*;
import utn.TpFinal.AppUnTN.model.Commentary;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Punctuation;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.DocumentService;
import utn.TpFinal.AppUnTN.service.PunctuationService;
import utn.TpFinal.AppUnTN.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/punctuations")
public class PunctuationController {

    private final PunctuationService punctuationService;
    private final DocumentService documentService;
    private final UserService userService;

    @Autowired
    public PunctuationController(PunctuationService punctuationService,
                                 DocumentService documentService,
                                 UserService userService) {
        this.punctuationService = punctuationService;
        this.documentService = documentService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<PunctuationDTO> addPunctuation(@RequestBody PunctuationRequestDTO request,
                                                         Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Document document = documentService.buscarPorId(request.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        Punctuation punctuation = new Punctuation();
        punctuation.setValue(request.getValue());
        punctuation.setDocument(document);
        punctuation.setAuthor(user);

        Punctuation saved = punctuationService.guardar(punctuation);
        return ResponseEntity.ok(PunctuationDTO.fromEntity(saved));
    }


    @PostMapping("/getByDocument")
    public ResponseEntity<List<PunctuationDTO>> getByDocument(@RequestBody IdRequest request) {
        Document document = documentService.buscarPorId(request.getId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        List<PunctuationDTO> dtos = punctuationService.listarPorDocumentoOrdenado(document).stream()
                .map(PunctuationDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/update")
    public ResponseEntity<PunctuationDTO> updatePunctuation(@RequestBody UpdatePunctuationRequest request) {
        Punctuation updated = punctuationService.actualizar(request.getId(), request.getNuevoValor());
        return ResponseEntity.ok(PunctuationDTO.fromEntity(updated));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deletePunctuation(@RequestBody IdRequest idRequest) {
        punctuationService.eliminar(idRequest.getId());
        return ResponseEntity.ok("Puntuacion eliminada correctamente");
    }

    @PostMapping("/average")
    public ResponseEntity<Double> getAverage(@RequestBody IdRequest request) {
        Document document = documentService.buscarPorId(request.getId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        double promedio = punctuationService.obtenerPromedioPorDocumento(document);
        return ResponseEntity.ok(promedio);
    }
}


