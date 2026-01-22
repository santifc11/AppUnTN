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
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ej: "Ingeniería en Sistemas"

    // Relación: Muchas Carreras -> Una Universidad
    @ManyToOne(fetch = FetchType.LAZY) // 4. LAZY: Solo trae la info de la universidad si se la pedimos explícitamente (ahorra memoria).
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    // Relación: Una Carrera -> Muchas Materias
    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Subject> subjects = new ArrayList<>();
}