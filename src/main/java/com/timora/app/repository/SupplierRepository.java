package com.timora.app.repository;

import com.timora.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {


    @Query("SELECT DISTINCT s FROM Supplier s " +
            "JOIN UserSupplierPermission usp ON usp.supplier.id = s.id " +
            "WHERE usp.user.id = :userId")
    List<Supplier> findByUserIdWithPermissions(@Param("userId") Long userId);

}