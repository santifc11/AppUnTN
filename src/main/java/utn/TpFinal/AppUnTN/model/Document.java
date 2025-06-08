package utn.TpFinal.AppUnTN.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String fileUrl;

    private String fileType; // Ej: "PDF", "DOCX", "TXT"

    @CreationTimestamp
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentary> commentaries;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Punctuation> punctuations;

    public double getAveragePunctuation() {
        if (punctuations == null || punctuations.isEmpty()) return 0.0;
        return punctuations.stream()
                .mapToInt(Punctuation::getValue)
                .average()
                .orElse(0.0);
    }
}
