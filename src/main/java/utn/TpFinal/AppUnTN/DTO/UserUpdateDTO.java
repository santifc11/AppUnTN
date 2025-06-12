package utn.TpFinal.AppUnTN.DTO;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UserUpdateDTO {
    private String name;
    private String lastname;
    private String mail;
    private String password;
    private String city;
    private String about;

}
