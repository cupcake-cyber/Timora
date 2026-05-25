package com.timora.app.service.impl;

import com.timora.app.model.UserSupplierRole;
import com.timora.app.model.UserSupplierRoleId;
import com.timora.app.repository.UserSupplierRoleRepository;
import com.timora.app.service.UserSupplierRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserSupplierRoleServiceImpl
        implements UserSupplierRoleService {

    private final UserSupplierRoleRepository userSupplierRoleRepository;

    public UserSupplierRoleServiceImpl(
            UserSupplierRoleRepository userSupplierRoleRepository
    ) {
        this.userSupplierRoleRepository = userSupplierRoleRepository;
    }

    @Override
    public List<UserSupplierRole> findAll() {

        return userSupplierRoleRepository.findAll();
    }

    @Override
    public UserSupplierRole findById(UserSupplierRoleId id) {

        return userSupplierRoleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "UserSupplierRole not found"
                        )
                );
    }

    @Override
    public List<UserSupplierRole> findByUserId(Long userId) {

        return userSupplierRoleRepository.findByUserId(userId);
    }

    @Override
    public List<UserSupplierRole> findBySupplierId(Long supplierId) {

        return userSupplierRoleRepository.findBySupplierId(supplierId);
    }

    @Override
    public List<UserSupplierRole> findByRoleId(Long roleId) {

        return userSupplierRoleRepository.findByRoleId(roleId);
    }

    @Override
    public UserSupplierRole save(
            UserSupplierRole userSupplierRole
    ) {

        return userSupplierRoleRepository.save(userSupplierRole);
    }

    @Override
    public void delete(UserSupplierRoleId id) {

        UserSupplierRole existing =
                findById(id);

        userSupplierRoleRepository.delete(existing);
    }
}