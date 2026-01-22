package utn.TpFinal.AppUnTN.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Clave primaria autoincremental

    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    // Relación: Una Universidad -> Muchas Carreras
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    @JsonIgnore // 3. Importante: Evita que al pedir la Uni, te traiga las carreras, y estas a la uni, creando un bucle infinito.
    private List<Career> careers = new ArrayList<>();
}