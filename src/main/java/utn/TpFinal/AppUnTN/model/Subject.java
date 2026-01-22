package utn.TpFinal.AppUnTN.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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