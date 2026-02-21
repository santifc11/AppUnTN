package utn.TpFinal.AppUnTN.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; //para pruebas postman
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //para pruebas postman
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Relación: Muchas Materias -> Una Carrera
    @ManyToOne(fetch = FetchType.EAGER) // 5. EAGER: Cuando cargue la materia, traerá automáticamente los datos de la carrera asociada.
    @JoinColumn(name = "career_id", nullable = false)
    private Career career;
}