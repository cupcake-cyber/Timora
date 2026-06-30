package com.timora.app.model;

import com.timora.app.model.enums.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserSupplierPermissionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "permission")
    private Permission permission;
}