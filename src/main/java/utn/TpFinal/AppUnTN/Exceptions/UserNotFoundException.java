package utn.TpFinal.AppUnTN.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String mensaje) {
        super(mensaje);
    }
}