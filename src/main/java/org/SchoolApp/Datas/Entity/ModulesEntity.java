package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@ToString
public class ModulesEntity  extends EntityAbstract {
    @Id
    @GeneratedValue
    private Long id;

    private String nom;

    private String description;

    private int duree_acquisition;

    @ManyToMany(mappedBy = "modules")
    @JsonIgnore
    @ToString.Exclude
    private List<CompetencesEntity> competences;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NotesEntity> notes;


}
