package com.timora.app.model.enums;

public enum GlobalRole {

    OWNER,          // Control total de la empresa/tenant

    COMPANY_ADMIN,  // Administración organizacional general

    STAFF,          // Usuario interno estándar sin autoridad organizacional

    SUPPORT,        // Soporte técnico u operacional interno

    AUDITOR         // Solo lectura/reportes/auditoría

}