package utn.TpFinal.AppUnTN.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.TpFinal.AppUnTN.model.Punctuation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunctuationDTO {
    private Long id;
    private int value;
    private String authorUsername;

    public static PunctuationDTO fromEntity(Punctuation p) {
        return new PunctuationDTO(
                p.getId(),
                p.getValue(),
                p.getAuthor().getUsername()
        );
    }
}