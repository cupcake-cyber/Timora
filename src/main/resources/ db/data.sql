USE timora;

-- =========================
-- COMPANY
-- =========================
INSERT INTO company (name, ruc, address, phone, email, status, created_at) VALUES
('Timora', '11111111111', 'HQ', '999999999', 'timora@timora.com', 'ACTIVE', NOW()),
('Company A', '22222222222', 'Address A', '999111111', 'a@company.com', 'ACTIVE', NOW()),
('Company B', '33333333333', 'Address B', '999222222', 'b@company.com', 'ACTIVE', NOW());

-- =========================
-- USERS (ONLY USERS TABLE FIRST)
-- =========================
INSERT INTO user (company_id, login_email, password_hash, status, global_role, created_at) VALUES
-- Timora OWNER
(1, 'owner@timora.com', '{noop}admin', 'ACTIVE', 'OWNER', NOW()),

-- Company A
(2, 'admin@a.com', '{noop}admin', 'ACTIVE', 'ADMIN', NOW()),
(2, 'supplier@a.com', '{noop}admin', 'ACTIVE', 'USER', NOW()),
(2, 'customer@a.com', '{noop}admin', 'ACTIVE', 'USER', NOW()),

-- Company B
(3, 'admin@b.com', '{noop}admin', 'ACTIVE', 'ADMIN', NOW()),
(3, 'supplier@b.com', '{noop}admin', 'ACTIVE', 'USER', NOW()),
(3, 'customer@b.com', '{noop}admin', 'ACTIVE', 'USER', NOW());

-- =========================
-- PERSON (MATCH USER IDS ORDER)
-- =========================
INSERT INTO person (company_id, user_id, first_name, last_name, status, phone, email, address, created_at) VALUES
-- Timora owner (user_id = 1)
(1, 1, 'Owner', 'Timora', 'ACTIVE', '999999999', 'owner@timora.com', 'HQ', NOW()),

-- Company A users
(2, 2, 'Admin', 'A', 'ACTIVE', '999111111', 'admin@a.com', 'Address A', NOW()),
(2, 3, 'Supplier', 'A', 'ACTIVE', '999111112', 'supplier@a.com', 'Address A', NOW()),
(2, 4, 'Customer', 'A', 'ACTIVE', '999111113', 'customer@a.com', 'Address A', NOW()),

-- Company B users
(3, 5, 'Admin', 'B', 'ACTIVE', '999222111', 'admin@b.com', 'Address B', NOW()),
(3, 6, 'Supplier', 'B', 'ACTIVE', '999222112', 'supplier@b.com', 'Address B', NOW()),
(3, 7, 'Customer', 'B', 'ACTIVE', '999222113', 'customer@b.com', 'Address B', NOW());

-- =========================
-- CUSTOMER (ONLY CUSTOMER TYPE)
-- =========================
INSERT INTO customer (company_id, person_id, notes, created_at) VALUES
(2, 4, 'Customer A', NOW()),
(3, 7, 'Customer B', NOW());

-- =========================
-- SUPPLIER (ONLY SUPPLIER TYPE)
-- =========================
INSERT INTO supplier (company_id, person_id, specialty, notes, created_at) VALUES
(2, 3, 'Barber A', 'Supplier A main', NOW()),
(3, 6, 'Barber B', 'Supplier B main', NOW());

-- =========================
-- ROLES
-- =========================
INSERT INTO role (company_id, name, description, created_at) VALUES
(1, 'OWNER_ROLE', 'Owner role', NOW()),
(2, 'ADMIN', 'Admin role', NOW()),
(2, 'SUPPLIER_ADMIN', 'Supplier admin', NOW()),
(3, 'ADMIN', 'Admin role', NOW()),
(3, 'SUPPLIER_ADMIN', 'Supplier admin', NOW());

-- =========================
-- PERMISSIONS
-- =========================
INSERT INTO permission (code, description) VALUES
('BOOKING_VIEW', 'Can view bookings'),
('BOOKING_EDIT', 'Can edit bookings'),
('BOOKING_CANCEL', 'Can cancel bookings'),
('SERVICE_EDIT', 'Can edit services'),
('AVAILABILITY_EDIT', 'Can edit availability');

-- =========================
-- ROLE PERMISSIONS (MINIMAL)
-- =========================
INSERT INTO role_permission (role_id, permission_id) VALUES
-- ADMIN (assume role_id 2,4)
(2,1),(2,2),(2,3),(2,4),(2,5),
(4,1),(4,2),(4,3),(4,4),(4,5),

-- SUPPLIER_ADMIN (assume role_id 3,5)
(3,1),(3,3),(3,5),
(5,1),(5,3),(5,5);

-- =========================
-- USER SUPPLIER ROLE
-- =========================
INSERT INTO user_supplier_role (user_id, supplier_id, role_id, assigned_by_user_id, created_at) VALUES
-- Company A
(3, 1, 3, 2, NOW()),

-- Company B
(6, 2, 5, 5, NOW());

-- =========================
-- SERVICE (2 per supplier)
-- =========================
INSERT INTO service (
    company_id,
    supplier_id,
    name,
    description,
    price,
    duration,
    status,
    created_at
)
VALUES

-- =========================
-- COMPANY A (supplier_id = 1)
-- =========================
(2, 1, 'Haircut Basic', 'Basic haircut service', 10.00, 30, 'ACTIVE', NOW()),
(2, 1, 'Beard Trim', 'Beard shaping and trim', 7.50, 20, 'ACTIVE', NOW()),

-- =========================
-- COMPANY B (supplier_id = 2)
-- =========================
(3, 2, 'Haircut Premium', 'Premium haircut with styling', 15.00, 45, 'ACTIVE', NOW()),
(3, 2, 'Full Grooming', 'Hair + beard + styling package', 25.00, 60, 'ACTIVE', NOW());

-- =========================

INSERT INTO availability (
    company_id,
    supplier_id,
    start_date,
    end_date,
    day_of_week,
    start_time,
    end_time,
    recurrence_type,
    slot_duration_minutes,
    capacity,
    status,
    notes,
    created_at
)
VALUES

-- =========================
-- BLOQUE MAÑANA (MON-FRI)
-- =========================

(2, 1, '2026-01-01', '2026-12-31', 'MONDAY',    '09:00:00', '12:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Morning shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'TUESDAY',   '09:00:00', '12:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Morning shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'WEDNESDAY', '09:00:00', '12:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Morning shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'THURSDAY',  '09:00:00', '12:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Morning shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'FRIDAY',    '09:00:00', '12:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Morning shift', NOW()),

-- =========================
-- BLOQUE TARDE (MON-FRI)
-- =========================

(2, 1, '2026-01-01', '2026-12-31', 'MONDAY',    '14:00:00', '18:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Afternoon shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'TUESDAY',   '14:00:00', '18:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Afternoon shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'WEDNESDAY', '14:00:00', '18:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Afternoon shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'THURSDAY',  '14:00:00', '18:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Afternoon shift', NOW()),
(2, 1, '2026-01-01', '2026-12-31', 'FRIDAY',    '14:00:00', '18:00:00', 'WEEKLY', 30, 1, 'ACTIVE', 'Afternoon shift', NOW());

-- =========================
-- BOOKINGS (valid inside availability)
-- Company A - supplier_id = 1
-- =========================
INSERT INTO booking (
    company_id,
    service_id,
    customer_id,
    created_by_user_id,
    start_time,
    end_time,
    status,
    type,
    name,
    description,
    created_at
)
VALUES

-- 1) BOOKED + PAID (morning slot)
(
    2,
    1,
    1,
    2,
    '2026-06-01 09:00:00',
    '2026-06-01 09:30:00',
    'CONFIRMED',
    'APPOINTMENT',
    'Haircut Morning',
    'Basic haircut booking',
    NOW()
),

-- 2) BOOKED (afternoon slot)
(
    2,
    2,
    1,
    2,
    '2026-06-01 14:00:00',
    '2026-06-01 14:30:00',
    'CONFIRMED',
    'APPOINTMENT',
    'Beard Trim',
    'Afternoon beard service',
    NOW()
),

-- 3) BOOKED (future slot)
(
    2,
    1,
    1,
    2,
    '2026-06-02 10:00:00',
    '2026-06-02 10:30:00',
    'PENDING',
    'APPOINTMENT',
    'Haircut Second Day',
    'Scheduled haircut',
    NOW()
);

-- =========================
-- PAYMENT (ONLY FOR FIRST BOOKING)
-- =========================
INSERT INTO payment (
    company_id,
    booking_id,
    amount,
    status,
    method,
    created_at
)
VALUES
(
    2,
    1,
    10.00,
    'PAID',
    'CASH',
    NOW()
);
