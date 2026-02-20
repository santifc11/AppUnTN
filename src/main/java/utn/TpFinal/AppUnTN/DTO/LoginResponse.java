package utn.TpFinal.AppUnTN.DTO;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private String role;

    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

}
