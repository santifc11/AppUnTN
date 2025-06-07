package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.DTO.LoginRequest;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.UserRepository;
import utn.TpFinal.AppUnTN.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller //estaba@RestController pero lo tuve que cambiar para que entre html.
@RequestMapping("/api/users")
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@ModelAttribute User user){ //Model Attribute para que acepte datos de formulario.
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.login(loginRequest    .getUsername(), loginRequest.getPassword());
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).build());
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


