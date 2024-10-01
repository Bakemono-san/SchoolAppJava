package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByLibelle(String libelle);
}

