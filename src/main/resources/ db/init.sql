-- =========================
-- Datos basicos para
-- inicializacion y login
-- =========================


USE timora;

INSERT INTO company (name, status, created_at)
VALUES ('Timora', 'ACTIVE', NOW());

INSERT INTO `user` (
    company_id,
    login_email,
    password_hash,
    status,
    global_role,
    created_at
) VALUES (
    1,
    'admin@timora.com',
    '{noop}admin',
    'ACTIVE',
    'OWNER',
    NOW()
);

