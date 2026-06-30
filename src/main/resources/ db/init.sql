USE timora;

-- =========================
-- COMPANY
-- =========================
INSERT INTO company (name,ruc,address,phone,email,status,created_at)
VALUES ('Timora','12345678901','HQ','999999999','owner@timora.com','ACTIVE',NOW());

INSERT INTO company (name, ruc, address, phone, email, status, created_at)
VALUES ('Nova Agenda', '20123456789', 'Oficina Central', '977777777', 'admin2@timora.com', 'ACTIVE', NOW());


-- =========================
-- OWNER (Company 1)
-- =========================
INSERT INTO person (company_id,first_name,last_name,status,phone,email,address,created_at)
VALUES (1,'Owner','Timora','ACTIVE','999999999','owner@timora.com','HQ',NOW());

INSERT INTO user (company_id,person_id,email,password_hash,status,global_role,created_at)
VALUES (1,1,'owner@timora.com','{noop}123','ACTIVE','OWNER',NOW());


-- =========================
-- COMPANY 2
-- =========================
SET @company_id = 2;

-- ADMIN USER
INSERT INTO person (company_id, first_name, last_name, status, phone, email, address, created_at)
VALUES (@company_id, 'Admin', 'User', 'ACTIVE', '999999991', 'admin2@timora.com', 'HQ', NOW());

SET @person_admin_user = LAST_INSERT_ID();

INSERT INTO user (company_id, person_id, email, password_hash, status, global_role, created_at)
VALUES (@company_id, @person_admin_user, 'admin2@timora.com', '{noop}123', 'ACTIVE', 'ADMIN', NOW());


-- ADMIN SUPPLIER
INSERT INTO person (company_id, first_name, last_name, status, phone, email, address, created_at)
VALUES (@company_id, 'Admin', 'Supplier', 'ACTIVE', '999999992', 'admin2_supplier@timora.com', 'HQ', NOW());

SET @person_admin_supplier = LAST_INSERT_ID();

INSERT INTO user (company_id, person_id, email, password_hash, status, global_role, created_at)
VALUES (@company_id, @person_admin_supplier, 'admin2_supplier@timora.com', '{noop}123', 'ACTIVE', 'ADMIN', NOW());

INSERT INTO supplier (company_id, person_id, specialty, notes, created_at)
VALUES (@company_id, @person_admin_supplier, 'General Services', 'Admin supplier account', NOW());


-- NORMAL USER
INSERT INTO person (company_id, first_name, last_name, status, phone, email, address, created_at)
VALUES (@company_id, 'User', 'One', 'ACTIVE', '999999993', 'user@timora.com', 'HQ', NOW());

SET @person_user = LAST_INSERT_ID();

INSERT INTO user (company_id, person_id, email, password_hash, status, global_role, created_at)
VALUES (@company_id, @person_user, 'user@timora.com', '{noop}123', 'ACTIVE', 'USER', NOW());


-- NORMAL SUPPLIER USER
INSERT INTO person (company_id, first_name, last_name, status, phone, email, address, created_at)
VALUES (@company_id, 'User', 'Supplier', 'ACTIVE', '999999994', 'user2@timora.com', 'HQ', NOW());

SET @person_user_supplier = LAST_INSERT_ID();

INSERT INTO user (company_id, person_id, email, password_hash, status, global_role, created_at)
VALUES (@company_id, @person_user_supplier, 'user2@timora.com', '{noop}123', 'ACTIVE', 'USER', NOW());

INSERT INTO supplier (company_id, person_id, specialty, notes, created_at)
VALUES (@company_id, @person_user_supplier, 'Basic Services', 'User supplier account', NOW());

USE timora;

-- =========================
-- CONFIGURATION (ALL USERS)
-- =========================

INSERT INTO configuration (
    user_id,
    notify_appointments,
    notify_reservations,
    notify_cancellations,
    notify_reminders,
    reminder_minutes_before,
    app_channel_enabled,
    email_channel_enabled,
    start_time_silence ,
    end_time_silence ,
    dark_mode
) VALUES

-- OWNER (1)
(1, true, true, true, true, 15, true, true, '22:00:00', '07:00:00', false),

-- ADMIN USER (2)
(2, true, true, true, true, 10, true, true, '22:00:00', '07:00:00', false),

-- ADMIN SUPPLIER (3)
(3, true, true, true, true, 10, true, true, '21:30:00', '07:00:00', false),

-- USER NORMAL (4)
(4, true, false, true, true, 20, true, false, '21:00:00', '08:00:00', false),

-- USER SUPPLIER (5)
(5, true, true, true, true, 5, true, true, '23:00:00', '06:00:00', true);




