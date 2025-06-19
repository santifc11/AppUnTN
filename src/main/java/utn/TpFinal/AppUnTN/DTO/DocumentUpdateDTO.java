package utn.TpFinal.AppUnTN.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.TpFinal.AppUnTN.model.Subject;
@Getter
@Setter
public class DocumentUpdateDTO {
    @NotNull(message = "El ID del documento no puede ser nulo.")
    private Long id;

    @NotBlank(message = "El título no puede estar vacío.")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía.")
    private String description;

    @NotNull(message = "La materia no puede ser nula.")
    private Subject subject;
}
