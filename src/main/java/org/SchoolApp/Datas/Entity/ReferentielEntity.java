package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class ReferentielEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String libelle;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    private String photo_couverture;

    @Enumerated(EnumType.STRING)
    private StatusReferenceEnum status;

    @ManyToMany(mappedBy = "referentiels",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<PromoEntity> promos = new HashSet<>();

    @OneToMany(mappedBy = "referentiel")
    private Set<ApprenantEntity> apprenants;

    @ManyToMany
    @JoinTable(
            name = "referentiel_competence",
            joinColumns = @JoinColumn(name = "referentiel_id"),
            inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    private Set<CompetencesEntity> competences;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserEntity other = (UserEntity) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return 31; // or use a constant or just return a unique identifier hash
    }
}