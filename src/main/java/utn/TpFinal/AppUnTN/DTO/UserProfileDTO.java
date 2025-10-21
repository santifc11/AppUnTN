package utn.TpFinal.AppUnTN.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String name;
    private String lastname;
    private String mail;
    private String city;
    private String about;
    private String role;
    private List<String> subjects;
    private List<DocumentResponseDTO> documents;
}


