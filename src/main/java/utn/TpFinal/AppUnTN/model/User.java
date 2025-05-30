package utn.TpFinal.AppUnTN.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

@Entity
@Getter @Setter @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String lastname;
    private String mail;
    private String username;
    private String password;
    private String city;
    @Lob //hace que el string pueda ser de mas de 255 caracteres.
    private String about;
    private Role role; //asigna el rol del usuario



}
