package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.TpFinal.AppUnTN.DTO.UserAdminDTO;
import utn.TpFinal.AppUnTN.DTO.UserUpdateDTO;
import utn.TpFinal.AppUnTN.Exceptions.UserAlreadyExistsException;
import utn.TpFinal.AppUnTN.model.Role;
import utn.TpFinal.AppUnTN.model.Subject;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.DocumentRepository;
import utn.TpFinal.AppUnTN.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final DocumentRepository documentRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DocumentRepository documentRepository) {
        this.userRepo = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.documentRepository = documentRepository;
    }

    //Le asigna un Id al user y lo guarda en la bdd.
    public User register(User user){
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya está registrado.");
        }
        if (userRepo.findByMail(user.getMail()).isPresent()) {
            throw new UserAlreadyExistsException("El email ya está registrado.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }



    public Optional<User> login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }


    public List<User> readAll() {
        return userRepo.findAll();
    }

    public List<UserAdminDTO> getAllUsersDTO() {
        return userRepo.findAll().stream()
                .map(u -> new UserAdminDTO(
                        u.getUsername(),
                        u.getName(),
                        u.getLastname(),
                        u.getMail(),
                        u.getRole().toString()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #usernameToDelete == authentication.name")
    public String deleteUser(String usernameRequester, String usernameToDelete) {
        Optional<User> requesterOpt = userRepo.findByUsername(usernameRequester);
        Optional<User> toDeleteOpt = userRepo.findByUsername(usernameToDelete);

        if (requesterOpt.isEmpty()) {
            return "Usuario solicitante no encontrado.";
        }
        if (toDeleteOpt.isEmpty()) {
            return "Usuario a eliminar no encontrado.";
        }

        User requester = requesterOpt.get();
        User toDelete = toDeleteOpt.get();

        documentRepository.deleteAllByAuthor(toDelete);

        if (usernameRequester.equals(usernameToDelete)) {
            userRepo.delete(toDelete);
            return "Usuario '" + usernameToDelete + "' eliminado con éxito (autodelete).";
        }

        if (requester.getRole() == Role.ADMIN) {
            userRepo.delete(toDelete);
            return "Usuario '" + usernameToDelete + "' eliminado con éxito por ADMIN.";
        }

        return "No autorizado para eliminar al usuario '" + usernameToDelete + "'.";
    }



    public String updateUserByUsername(String username, UserUpdateDTO updatedUserData) {
        return userRepo.findByUsername(username)
                .map(existingUser -> {
                    if (updatedUserData.getName() != null && !updatedUserData.getName().isBlank()) {
                        existingUser.setName(updatedUserData.getName());
                    }
                    if (updatedUserData.getLastname() != null && !updatedUserData.getLastname().isBlank()) {
                        existingUser.setLastname(updatedUserData.getLastname());
                    }
                    if (updatedUserData.getMail() != null && !updatedUserData.getMail().isBlank()) {
                        if (!updatedUserData.getMail().equals(existingUser.getMail())) {
                            Optional<User> mailOwner = userRepo.findByMail(updatedUserData.getMail());
                            if (mailOwner.isPresent() && !mailOwner.get().getUsername().equals(username)) {
                                throw new UserAlreadyExistsException("El email ya está en uso por otro usuario.");
                            }
                        }
                        existingUser.setMail(updatedUserData.getMail());
                    }
                    if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
                        String encodedPassword = passwordEncoder.encode(updatedUserData.getPassword());
                        existingUser.setPassword(encodedPassword);
                    }
                    if (updatedUserData.getCity() != null && !updatedUserData.getCity().isBlank()) {
                        existingUser.setCity(updatedUserData.getCity());
                    }
                    if (updatedUserData.getAbout() != null && !updatedUserData.getAbout().isBlank()) {
                        existingUser.setAbout(updatedUserData.getAbout());
                    }

                    userRepo.save(existingUser);
                    return "Usuario '" + username + "' actualizado con éxito.";
                })
                .orElse("Usuario '" + username + "' no encontrado.");
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<Subject> getSubjectsOfProfessor(String username) {
        return userRepo.findByUsername(username)
                .filter(user -> user.getRole() == Role.PROFESSOR)
                .map(User::getSubjects)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o no es profesor"));
    }

    public String updateSubjects(String username, List<Subject> subjects) {
        return userRepo.findByUsername(username)
                .map(user -> {
                    if (user.getRole() != Role.PROFESSOR) {
                        throw new RuntimeException("Solo los profesores pueden tener materias asignadas");
                    }

                    List<Subject> currentSubjects = user.getSubjects();

                    for (Subject subject : subjects) {
                        if (!currentSubjects.contains(subject)) {
                            currentSubjects.add(subject);
                        }
                    }

                    user.setSubjects(currentSubjects);
                    userRepo.save(user);
                    return "Materias agregadas correctamente";
                })
                .orElse("Usuario no encontrado");
    }

    public String deleteSubject(String username, String subjectStr) {
        Subject subject;

        try {
            subject = Subject.valueOf(subjectStr.toUpperCase()); // Convierte a mayúsculas para mayor tolerancia
        } catch (IllegalArgumentException | NullPointerException e) {
            return "Materia inválida: " + subjectStr;
        }

        return userRepo.findByUsername(username)
                .map(user -> {
                    if (user.getRole() != Role.PROFESSOR) {
                        throw new RuntimeException("Solo los profesores pueden tener materias asignadas");
                    }

                    List<Subject> subjects = user.getSubjects();

                    if (subjects.contains(subject)) {
                        subjects.remove(subject);
                        user.setSubjects(subjects);
                        userRepo.save(user);
                        return "Materia eliminada correctamente";
                    } else {
                        return "El profesor no tiene asignada la materia: " + subject;
                    }
                })
                .orElse("Usuario no encontrado");
    }
}

