package com.timora.app.service.impl;

import com.timora.app.model.Role;
import com.timora.app.repository.RoleRepository;
import com.timora.app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {

        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role name already exists");
        }

        return roleRepository.save(role);
    }

    @Override
    public Role update(Long id, Role role) {

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        existing.setCompany(role.getCompany());

        return roleRepository.save(existing);
    }

    @Override
    public void delete(Long id) {

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        roleRepository.delete(existing);
    }

}
