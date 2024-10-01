package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class ReferentielEntity extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String libelle;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private String photoCouverture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReferenceEnum status = StatusReferenceEnum.Inactif; // Set default value to 'Inactif'


    @ManyToMany(mappedBy = "referentiels",fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<PromoEntity> promos = new HashSet<>();

    @OneToMany(mappedBy = "referentiel")
    @JsonIgnore
    private Set<ApprenantEntity> apprenants;

    @ManyToMany
    @JoinTable(
            name = "referentiel_competence",
            joinColumns = @JoinColumn(name = "referentiel_id"),
            inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    @JsonIgnore
    private Set<CompetencesEntity> competences;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReferentielEntity other = (ReferentielEntity) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return 31; // or use a constant or just return a unique identifier hash
    }
}