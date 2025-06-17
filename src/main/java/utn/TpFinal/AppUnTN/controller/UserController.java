package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.DTO.*;
import utn.TpFinal.AppUnTN.model.Subject;
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

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
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

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String username = authentication.getName(); // extrae el nombre de usuario del token JWT
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subjects/get")
    public ResponseEntity<List<Subject>> getSubjectsOfProfessor(Authentication auth) {
        String username = auth.getName();
        List<Subject> subjects = userService.getSubjectsOfProfessor(username);
        return ResponseEntity.ok(subjects);
    }

    @PutMapping("/subjects/update")
    public ResponseEntity<String> updateSubjects(Authentication auth, @RequestBody SubjectsDTO dto) {
        String username = auth.getName();
        String resultado = userService.updateSubjects(username, dto.getSubjects());
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/subjects/delete")
    public ResponseEntity<String> deleteSubject(Authentication auth, @RequestBody FilterSubjectDTO dto) {
        String username = auth.getName();
        String result = userService.deleteSubject(username, dto.getSubject());
        return ResponseEntity.ok(result);
    }

}


