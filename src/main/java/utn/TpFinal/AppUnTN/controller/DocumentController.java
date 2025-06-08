package utn.TpFinal.AppUnTN.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public List<Document> list() {
        return documentService.findAll();
    }

    @GetMapping("/{id}")
    public Document get(@PathVariable Long id) {
        return documentService.findById(id);
    }

    @PostMapping
    public Document upload(@RequestBody Document document, @AuthenticationPrincipal User user) {
        return documentService.upload(document, user);
    }
}
