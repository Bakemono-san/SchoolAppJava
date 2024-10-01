package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString
public class CompetencesEntity extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;
    private String description;
    private int duree_acquisition;
    private String type;

    @ManyToMany(mappedBy = "competences")
    @JsonIgnore
    private List<ReferentielEntity> referentiels;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "competence_module",
            joinColumns = @JoinColumn(name = "competence_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    @JsonIgnore
    @ToString.Exclude
    private List<ModulesEntity> modules;
}
