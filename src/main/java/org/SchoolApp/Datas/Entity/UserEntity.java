package org.SchoolApp.Datas.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.SchoolApp.Datas.Enums.StatusEnum;

import java.util.Set;
import org.odc.core.Datas.Entity.EntityAbstract;

@Data
@Entity
@ToString
public class UserEntity extends EntityAbstract{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String adresse;

    @Column(unique = true, nullable = false)
    private String Telephone;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = true)
    @JsonIgnore
    @ToString.Exclude
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "fonction_id", nullable = true)
    @JsonIgnore
    @ToString.Exclude
    private FonctionEntity fonction;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private ApprenantEntity apprenant;

    @ManyToMany
    @JoinTable(
            name = "user_emargement",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "emargement_id")
    )
    @JsonIgnore
    private Set<EmargementEntity> emargements;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserEntity other = (UserEntity) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31; // or use a constant or just return a unique identifier hash
    }
}
