package com.timora.app.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSupplierRoleId implements Serializable {

    private Long userId;
    private Long supplierId;
    private Long roleId;
}