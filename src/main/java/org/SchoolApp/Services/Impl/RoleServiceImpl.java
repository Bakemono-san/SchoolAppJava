package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.Role;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Services.Interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleByLibelle(String libelle) {
        return roleRepository.findByLibelle(libelle);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
