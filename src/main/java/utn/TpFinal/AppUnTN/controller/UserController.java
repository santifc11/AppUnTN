package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.DTO.UserUpdateDTO;
import utn.TpFinal.AppUnTN.DTO.UsernameRequest;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.service.UserService;

import java.util.List;

@Controller //estaba@RestController pero lo tuve que cambiar para que entre html.
@RequestMapping("/api/users")
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping(
            value = "/register",
            consumes = {"application/json", "application/x-www-form-urlencoded"}
    )
    public ResponseEntity<User> register(@RequestBody(required = false) User userFromJson,
                                         @ModelAttribute User userFromForm) {
        User user = userFromJson != null ? userFromJson : userFromForm;
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.readAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody UsernameRequest usernameRequest) {
        String usernameToDelete = usernameRequest.getUsername();
        String usernameRequester = SecurityContextHolder.getContext().getAuthentication().getName();

        String result = userService.deleteUser(usernameRequester, usernameToDelete);

        if (result.contains("eliminado con éxito")) {
            return ResponseEntity.ok(result);
        } else if (result.contains("No autorizado")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }




    @PutMapping("/updateUser")
    public ResponseEntity<String> updateAuthenticatedUser(@RequestBody UserUpdateDTO updatedData) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String result = userService.updateUserByUsername(username, updatedData);

        if (result.contains("actualizado")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }





}


