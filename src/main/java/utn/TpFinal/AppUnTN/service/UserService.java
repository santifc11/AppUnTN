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
}

