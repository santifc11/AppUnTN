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

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        String result = userService.deleteUserByUsername(username);
        if (result != null) {
            return ResponseEntity.ok(result); // HTTP 200
        } else {
            return ResponseEntity.status(404).body("Usuario '" + username + "' no encontrado."); // HTTP 404
        }
    }


    @PutMapping("/updateUser/{username}")
    public ResponseEntity<String> updateUserByUsername(
            @PathVariable String username,
            @RequestBody User updatedUserData
    ) {
        String result = userService.updateUserByUsername(username, updatedUserData);
        if (result.contains("actualizado")) {
            return ResponseEntity.ok(result); // 200 OK
        } else {
            return ResponseEntity.status(404).body(result); // 404 Not Found
        }
    }



}
