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
