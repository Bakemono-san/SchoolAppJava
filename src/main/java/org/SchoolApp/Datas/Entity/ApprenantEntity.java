package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class ApprenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom_tuteur;
    private String prenom_tuteur;
    private String contact_tuteur;
    private String cni_file;
    private String extrait_file;
    private String diplome_file;
    private String casier_file;
    private String photo_couverture;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "etat_id", nullable = true)
    private EtatApprenant etatApprenant;

    @ManyToOne
    @JoinColumn(name = "referentiel_id", nullable = false)
    private ReferentielEntity referentiel;

    @OneToMany(mappedBy = "apprenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NotesEntity> notes;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ApprenantEntity other = (ApprenantEntity) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31; // or use a constant or just return a unique identifier hash
    }
}
