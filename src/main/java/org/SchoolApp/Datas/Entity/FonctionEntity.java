package org.SchoolApp.Datas.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.util.List;

@Data
@Entity
@ToString
public class FonctionEntity extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    private String description;

    @OneToMany(mappedBy = "fonction")
    @ToString.Exclude
    private List<UserEntity> users;
}
