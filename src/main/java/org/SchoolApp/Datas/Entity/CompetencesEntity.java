package org.SchoolApp.Datas.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CompetencesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String nom;
    private String description;
    private int duree_acquisition;
    private String type;

    @ManyToMany(mappedBy = "competences")
    private List<ReferentielEntity> referentiels;

    @ManyToMany
    @JoinTable(name = "competence_module",
            joinColumns = @JoinColumn(name = "competence_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private List<ModulesEntity> modules;
}
