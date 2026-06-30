package com.timora.app.repository;

import com.timora.app.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    // Filtrar solo los que NO son INACTIVE
    @Query("SELECT s FROM Service s WHERE s.company.id = :companyId AND s.status != 'INACTIVE'")
    List<Service> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT s FROM Service s WHERE s.supplier.id = :supplierId AND s.status != 'INACTIVE'")
    List<Service> findBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT s FROM Service s WHERE s.company.id = :companyId AND s.supplier.id = :supplierId AND s.status != 'INACTIVE'")
    List<Service> findByCompanyIdAndSupplierId(@Param("companyId") Long companyId, @Param("supplierId") Long supplierId);

    @Query("SELECT s FROM Service s WHERE s.supplier.person.id = :personId AND s.status != 'INACTIVE'")
    List<Service> findBySupplierPersonId(@Param("personId") Long personId);

    // Para owner que ve todos los NO INACTIVE
    @Query("SELECT s FROM Service s WHERE s.status != 'INACTIVE'")
    List<Service> findAllActive();
}