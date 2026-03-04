package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.DTO.DocumentResponseDTO;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document guardar(Document document) {
        document.setUploadDate(LocalDate.now());
        return documentRepository.save(document);
    }

    public List<Document> listarTodos() {
        return documentRepository.findAllWithDetails();
    }

    public Optional<Document> buscarPorId(Long id) {
        return documentRepository.findByIdWithDetails(id);
    }

    public void eliminar(Long id) {
        documentRepository.deleteById(id);
    }

    public DocumentResponseDTO mapToDTO(Document document) {
        List<DocumentResponseDTO.PunctuationDTO> punctuationDTOs = document.getPunctuations().stream()
                .map(p -> new DocumentResponseDTO.PunctuationDTO(
                        p.getId(),
                        p.getValue(),
                        p.getAuthor().getUsername(),
                        p.isDestacado()
                )).toList();

        List<DocumentResponseDTO.CommentaryDTO> commentaryDTOs = document.getCommentaries().stream()
                .map(c -> new DocumentResponseDTO.CommentaryDTO(
                        c.getId(),
                        c.getContent(),
                        c.getAuthor().getUsername(),
                        c.getCreationDate(),
                        c.isDestacado()
                )).toList();

        return new DocumentResponseDTO(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                document.getSubject().getId(),
                document.getSubject().getName(),
                document.getFileType(),
                document.getUploadDate(),
                document.getAuthor().getUsername(),
                document.getDownloadCount(),
                punctuationDTOs,
                commentaryDTOs
        );
    }

    public Document incrementarDescargas(Document document) {
        document.setDownloadCount(document.getDownloadCount() + 1);
        return documentRepository.save(document);
    }

    public List<Document> findBySubject(Subject subject){
        return documentRepository.findBySubjectWithDetails(subject);
    }

    public List<Document> findByAuthor(User author) {
        return documentRepository.findByAuthorWithDetails(author);
    }
}
