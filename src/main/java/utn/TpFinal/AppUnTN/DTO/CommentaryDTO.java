package utn.TpFinal.AppUnTN.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.TpFinal.AppUnTN.model.Commentary;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryDTO {
    private Long id;
    private String content;
    private String authorUsername;
    private LocalDate creationDate;
    private boolean destacado;

    public static CommentaryDTO fromEntity(Commentary c) {
        CommentaryDTO dto = new CommentaryDTO();
        dto.setId(c.getId());
        dto.setContent(c.getContent());
        dto.setAuthorUsername(c.getAuthor().getUsername());
        dto.setCreationDate(c.getCreationDate());
        dto.setDestacado(c.isDestacado());
        return dto;
    }
}
