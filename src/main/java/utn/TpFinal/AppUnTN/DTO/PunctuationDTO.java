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
    private boolean destacado; // nuevo

    public static PunctuationDTO fromEntity(Punctuation p) {
        PunctuationDTO dto = new PunctuationDTO();
        dto.setId(p.getId());
        dto.setValue(p.getValue());
        dto.setAuthorUsername(p.getAuthor().getUsername());
        dto.setDestacado(p.isDestacado()); // nuevo
        return dto;
    }
}