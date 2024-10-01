package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class EtatApprenant extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String etat;

    @Column(nullable = false)
    private String motif;

    @OneToMany(mappedBy = "etatApprenant")
    @JsonIgnore
    private List<ApprenantEntity> apprenants;
}
