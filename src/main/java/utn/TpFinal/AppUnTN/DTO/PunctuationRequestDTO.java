package utn.TpFinal.AppUnTN.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunctuationRequestDTO {
    private Long documentId;
    private int value;
}