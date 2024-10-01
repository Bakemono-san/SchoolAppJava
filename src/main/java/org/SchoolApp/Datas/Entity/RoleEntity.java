package org.SchoolApp.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.odc.core.Datas.Entity.EntityAbstract;

import java.util.List;

@Data
@Entity
@ToString
public class RoleEntity extends EntityAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    @ToString.Exclude
    private List<UserEntity> users;
}
