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
    @Transactional(readOnly = true)
    public List<UserSupplierRole> findAll() {

        return userSupplierRoleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserSupplierRole findById(UserSupplierRoleId id) {

        return userSupplierRoleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "UserSupplierRole not found"
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSupplierRole> findByUserId(Long userId) {

        return userSupplierRoleRepository
                .findByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSupplierRole> findBySupplierId(Long supplierId) {

        return userSupplierRoleRepository
                .findBySupplier_Id(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSupplierRole> findByRoleId(Long roleId) {

        return userSupplierRoleRepository
                .findByRole_Id(roleId);
    }

    @Override
    public UserSupplierRole save(
            UserSupplierRole userSupplierRole
    ) {

        return userSupplierRoleRepository
                .save(userSupplierRole);
    }

    @Override
    public void delete(UserSupplierRoleId id) {

        UserSupplierRole existing = findById(id);

        userSupplierRoleRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findSupplierIdsByUserId(Long userId) {

        return userSupplierRoleRepository
                .findSupplierIdsByUserId(userId);
    }
}