package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.DTO.*;
import utn.TpFinal.AppUnTN.Exceptions.UnauthorizedActionException;
import utn.TpFinal.AppUnTN.Exceptions.UserNotFoundException;
import utn.TpFinal.AppUnTN.model.Document;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.UserRepository;
import utn.TpFinal.AppUnTN.service.DocumentService;
import utn.TpFinal.AppUnTN.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // 👈 importante si tu front corre en Angular local
public class UserController {


    private final UserService userService;
    private final DocumentService documentService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, DocumentService documentService, UserRepository userRepository) {
        this.userService = userService;
        this.documentService = documentService;
        this.userRepository = userRepository;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.register(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserAdminDTO>> getAllUsers() {
        List<UserAdminDTO> users = userService.getAllUsersDTO();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody UsernameRequest usernameRequest) {
        String usernameToDelete = usernameRequest.getUsername();
        String usernameRequester = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            String result = userService.deleteUser(usernameRequester, usernameToDelete);

            if (result.contains("eliminado con éxito")) {
                return ResponseEntity.ok(result);
            } else if (result.contains("No autorizado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedActionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }


    @PutMapping("/updateUser")
    public ResponseEntity<String> updateAuthenticatedUser(@RequestBody UserUpdateDTO updatedData) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            String result = userService.updateUserByUsername(username, updatedData);
            if (result.contains("actualizado")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado");
        }

        String username = authentication.getName();
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

    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestBody UsernameRequest request) {
        String username = request.getUsername();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User user = userOpt.get();
        List<Document> docs = documentService.findByAuthor(user);

        UserProfileDTO profile = new UserProfileDTO(
                user.getName(),
                user.getLastname(),
                user.getMail(),
                user.getCity(),
                user.getAbout(),
                user.getRole().name(),
                user.getSubjects().stream().map(Enum::name).toList(),
                docs.stream().map(documentService::mapToDTO).toList()
        );

        return ResponseEntity.ok(profile);
    }

    // ✅ Verificar si el email ya existe
    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByMail(email);
        return ResponseEntity.ok(exists);
    }

    // ✅ Verificar si el username ya existe
    @GetMapping("/exists/username")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

}


