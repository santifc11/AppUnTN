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
    private LocalDate creationDate;
    private String authorUsername;

    public static CommentaryDTO fromEntity(Commentary c) {
        return new CommentaryDTO(
                c.getId(),
                c.getContent(),
                c.getCreationDate(),
                c.getAuthor().getUsername()
        );
    }
}
