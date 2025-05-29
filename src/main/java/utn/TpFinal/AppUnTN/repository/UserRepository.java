package utn.TpFinal.AppUnTN.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.TpFinal.AppUnTN.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
