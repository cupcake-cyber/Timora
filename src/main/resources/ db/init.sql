USE timora;

-- =========================
-- COMPANY
-- =========================

INSERT INTO company (name,ruc,address,phone,email,status,created_at)
VALUES ('Timora','12345678901','HQ','999999999','admin@timora.com','ACTIVE',NOW());

-- =========================
-- OWNER
-- password: admin123
-- =========================

INSERT INTO person (company_id,first_name,last_name,status,phone,email,address,created_at)
VALUES (1,'Admin','Timora','ACTIVE','999999999','admin@timora.com','HQ',NOW());

INSERT INTO user (company_id,person_id,email,password_hash,status,global_role,created_at)
VALUES (1,1,'admin@timora.com','{noop}admin','ACTIVE','OWNER',NOW());



