package utn.TpFinal.AppUnTN.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentaryRequest {
    private Long id;
    private String nuevoContenido;
}