package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.DTO.CommentaryDTO;
import utn.TpFinal.AppUnTN.DTO.DocumentResponseDTO;
import utn.TpFinal.AppUnTN.DTO.PunctuationDTO;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document guardar(Document document) {
        document.setUploadDate(LocalDate.now());
        return documentRepository.save(document);
    }

    public List<Document> listarTodos() {
        return documentRepository.findAll();
    }

    public Optional<Document> buscarPorId(Long id) {
        return documentRepository.findById(id);
    }

    public void eliminar(Long id) {
        documentRepository.deleteById(id);
    }

    public DocumentResponseDTO mapToDTO(Document document) {
        List<DocumentResponseDTO.PunctuationDTO> punctuationDTOs = document.getPunctuations().stream()
                .map(p -> new DocumentResponseDTO.PunctuationDTO(
                        p.getId(),
                        p.getValue(),
                        p.getAuthor().getUsername()
                )).toList();

        List<DocumentResponseDTO.CommentaryDTO> commentaryDTOs = document.getCommentaries().stream()
                .map(c -> new DocumentResponseDTO.CommentaryDTO(
                        c.getId(),
                        c.getContent(),
                        c.getAuthor().getUsername(),
                        c.getCreationDate()
                )).toList();

        return new DocumentResponseDTO(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                document.getFileType(),
                document.getUploadDate(),
                document.getAuthor().getUsername(),
                punctuationDTOs,
                commentaryDTOs
        );
    }



}
