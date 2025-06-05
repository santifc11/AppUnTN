package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);


}
