package utn.TpFinal.AppUnTN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.TpFinal.AppUnTN.model.User;
import utn.TpFinal.AppUnTN.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    //Le asigna un Id al user y lo guarda en la bdd.
    public User register(User user){
        return userRepo.save(user);
    }

    public Optional<User> login(String username, String password){
        return userRepo.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
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




}
