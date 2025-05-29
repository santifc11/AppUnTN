package utn.TpFinal.AppUnTN.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    private String name;
    private String lastname;
    private String mail;
    private String user;
    private String password;
    private String city;
    @Lob //hace que el string pueda ser de mas de 255 caracteres.
    private String about;


}
