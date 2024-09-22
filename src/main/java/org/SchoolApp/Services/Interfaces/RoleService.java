package org.SchoolApp.Services.Interfaces;

import org.SchoolApp.Datas.Entity.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role getRoleByLibelle(String libelle);
    List<Role> getAllRoles();
}
