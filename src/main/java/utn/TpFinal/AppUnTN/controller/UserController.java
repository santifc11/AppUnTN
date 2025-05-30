package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public String login(@RequestBody User login) {
        return userService.login(login.getUsername(), login.getPassword())
                .map(user -> "Bienvenido, " + user.getName())
                .orElse("Las credenciales ingresadas son incorrectas.");
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.readAll();
        return ResponseEntity.ok(users);
    }
}
