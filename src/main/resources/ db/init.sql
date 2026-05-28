USE timora;

-- =========================
-- COMPANY
-- =========================

INSERT INTO company (
    name,
    ruc,
    address,
    phone,
    email,
    status,
    created_at
)
VALUES (
    'Timora',
    '12345678901',
    'HQ',
    '999999999',
    'admin@timora.com',
    'ACTIVE',
    NOW()
);

-- =========================
-- USER
-- =========================

INSERT INTO user (
    company_id,
    login_email,
    password_hash,
    status,
    global_role,
    created_at
)
VALUES (
    1,
    'admin@timora.com',
    '{noop}admin',
    'ACTIVE',
    'OWNER',
    NOW()
);

-- password real:
-- admin123

-- =========================
-- PERSON
-- =========================

INSERT INTO person (
    company_id,
    user_id,
    first_name,
    last_name,
    status,
    phone,
    email,
    address,
    created_at
)
VALUES (
    1,
    1,
    'Admin',
    'Timora',
    'ACTIVE',
    '999999999',
    'admin@timora.com',
    'HQ',
    NOW()
);

-- =========================
-- SUPPLIER PERSON
-- =========================

INSERT INTO person (
    company_id,
    first_name,
    last_name,
    status,
    phone,
    email,
    address,
    created_at
)
VALUES (
    1,
    'Carlos',
    'Barber',
    'ACTIVE',
    '988888888',
    'carlos@timora.com',
    'Sucursal 1',
    NOW()
);

-- =========================
-- SUPPLIER
-- =========================

INSERT INTO supplier (
    company_id,
    person_id,
    specialty,
    notes,
    created_at
)
VALUES (
    1,
    2,
    'Barber',
    'Main barber supplier',
    NOW()
);

-- =========================
-- ROLES
-- =========================

INSERT INTO role (
    company_id,
    name,
    description,
    created_at
)
VALUES
(
    1,
    'SUPPLIER_ADMIN',
    'Admin supplier role',
    NOW()
),
(
    1,
    'SUPPLIER_STAFF',
    'Staff supplier role',
    NOW()
);

-- =========================
-- PERMISSIONS
-- =========================

INSERT INTO permission (
    code,
    description
)
VALUES
(
    'BOOKING_VIEW',
    'Can view bookings'
),
(
    'BOOKING_EDIT',
    'Can edit bookings'
),
(
    'BOOKING_CANCEL',
    'Can cancel bookings'
),
(
    'SERVICE_EDIT',
    'Can edit services'
),
(
    'AVAILABILITY_EDIT',
    'Can edit availability'
);

-- =========================
-- ROLE PERMISSIONS
-- =========================

-- SUPPLIER_ADMIN

INSERT INTO role_permission (
    role_id,
    permission_id
)
VALUES
(1,1),
(1,2),
(1,3),
(1,4),
(1,5);

-- SUPPLIER_STAFF

INSERT INTO role_permission (
    role_id,
    permission_id
)
VALUES
(2,1),
(2,3);

-- =========================
-- USER SUPPLIER ROLE
-- =========================

INSERT INTO user_supplier_role (
    user_id,
    supplier_id,
    role_id,
    assigned_by_user_id,
    created_at
)
VALUES (
    1,
    1,
    1,
    1,
    NOW()
);