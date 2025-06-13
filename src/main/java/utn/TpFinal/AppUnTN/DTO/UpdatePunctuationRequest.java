package utn.TpFinal.AppUnTN.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePunctuationRequest {
    private Long id;
    private int nuevoValor;
}