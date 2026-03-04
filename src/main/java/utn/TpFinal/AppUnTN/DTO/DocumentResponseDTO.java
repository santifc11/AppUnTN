package utn.TpFinal.AppUnTN.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DocumentResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Long subjectId;
    private String subject;
    private String fileType;
    private LocalDate uploadDate;
    private String authorUsername;
    private int downloadCount;
    private List<PunctuationDTO> punctuations;
    private List<CommentaryDTO> commentaries;

    @Data
    @AllArgsConstructor
    public static class PunctuationDTO {
        private Long id;
        private int value;
        private String authorUsername;
        private boolean destacado;
    }

    @Data
    @AllArgsConstructor
    public static class CommentaryDTO {
        private Long id;
        private String content;
        private String authorUsername;
        private LocalDate creationDate;
        private boolean destacado;
    }
}
