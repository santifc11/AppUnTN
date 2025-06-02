package utn.TpFinal.AppUnTN.model;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utn.TpFinal.AppUnTN.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class Administrator_0 implements CommandLineRunner {

    private UserRepository userRepo;

    @Override
    public void run(String... args){
        String email="admin0@gmail.com";
        if(userRepo.findByMail(email).isEmpty()){
            User Admin0=new User();
            Admin0.setName("Felipe");
            Admin0.setLastname("Climent");
            Admin0.setUsername("Admin0");
            Admin0.setMail(email);
            Admin0.setCity("San Manuel");
            Admin0.setPassword("admin0");
            Admin0.setRole(Role.ADMIN);
            Admin0.setAbout("Admin 0");
            userRepo.save(Admin0);
            System.out.println("Admin 0 creado exitosamente!");
        }else {
            System.out.println("Admin 0 ya existía!");
        }

    }
}
