package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.odc.core.Datas.Entity.EntityAbstract;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@ToString
public class PromoEntity extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;

    private Date date_debut;
    private Date date_fin;

    private int duree;

    @Enumerated(EnumType.STRING)
    private EtatEnum etat;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "promotion_referentiel",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "referentiel_id")
    )
    @Where(clause = "deleted = false")
    private Set<ReferentielEntity> referentiels = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PromoEntity other = (PromoEntity) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
