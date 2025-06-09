package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    //Le asigna un Id al user y lo guarda en la bdd.
    public User register(User user){
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


    public String deleteUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(user -> {
                    userRepo.delete(user);
                    return "Usuario '" + username + "' eliminado con éxito.";
                })
                .orElse(null);
    }

    public String updateUserByUsername(String username, User updatedUserData) {
        return userRepo.findByUsername(username)
                .map(existingUser -> {
                    // Actualizar campos (excepto username)
                    existingUser.setName(updatedUserData.getName());
                    existingUser.setLastname(updatedUserData.getLastname());
                    existingUser.setMail(updatedUserData.getMail());
                    existingUser.setPassword(updatedUserData.getPassword());
                    existingUser.setCity(updatedUserData.getCity());
                    existingUser.setAbout(updatedUserData.getAbout());
                    existingUser.setRole(updatedUserData.getRole());

                    userRepo.save(existingUser);
                    return "Usuario '" + username + "' actualizado con éxito.";
                })
                .orElse("Usuario '" + username + "' no encontrado.");
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

}

