DROP DATABASE timora;
CREATE DATABASE IF NOT EXISTS timora;
USE timora;

-- =========================
-- COMPANY
-- =========================
CREATE TABLE company (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255),
                         ruc VARCHAR(50),
                         address VARCHAR(255),
                         phone VARCHAR(30),
                         email VARCHAR(255),
                         status VARCHAR(20),
                         created_at DATETIME
);

-- =========================
-- PERSON
-- =========================
CREATE TABLE person (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        company_id BIGINT,
                        first_name VARCHAR(100),
                        last_name VARCHAR(100),
                        status VARCHAR(20),
                        phone VARCHAR(30),
                        email VARCHAR(255),
                        address VARCHAR(255),
                        created_at DATETIME,

                        FOREIGN KEY (company_id) REFERENCES company(id)
);

-- =========================
-- USER
-- =========================
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      company_id BIGINT,
                      person_id BIGINT UNIQUE,
                      email VARCHAR(255),
                      password_hash VARCHAR(255),
                      status VARCHAR(20),
                      global_role VARCHAR(20),
                      last_login_at DATETIME,
                      created_at DATETIME,

                      FOREIGN KEY (company_id) REFERENCES company(id),
                      FOREIGN KEY (person_id) REFERENCES person(id)
);

-- =========================
-- CUSTOMER
-- =========================
CREATE TABLE customer (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          company_id BIGINT,
                          person_id BIGINT UNIQUE,
                          notes TEXT,
                          created_at DATETIME,

                          FOREIGN KEY (company_id) REFERENCES company(id),
                          FOREIGN KEY (person_id) REFERENCES person(id)
);

-- =========================
-- SUPPLIER
-- =========================
CREATE TABLE supplier (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          company_id BIGINT,
                          person_id BIGINT UNIQUE,
                          specialty VARCHAR(255),
                          notes TEXT,
                          created_at DATETIME,

                          FOREIGN KEY (company_id) REFERENCES company(id),
                          FOREIGN KEY (person_id) REFERENCES person(id)
);

-- =========================
-- USER_SUPPLIER_PERMISSION
-- =========================
CREATE TABLE user_supplier_permission (
                                          user_id BIGINT,
                                          supplier_id BIGINT,
                                          permission VARCHAR(50),
                                          assigned_by_user_id BIGINT,
                                          created_at DATETIME,

                                          PRIMARY KEY (user_id, supplier_id, permission),

                                          FOREIGN KEY (user_id) REFERENCES user(id),
                                          FOREIGN KEY (supplier_id) REFERENCES supplier(id),
                                          FOREIGN KEY (assigned_by_user_id) REFERENCES user(id)
);

-- =========================
-- SERVICE
-- =========================
CREATE TABLE service (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         company_id BIGINT,
                         supplier_id BIGINT,
                         name VARCHAR(255),
                         description TEXT,
                         price DECIMAL(10,2),
                         duration INT,
                         status VARCHAR(20),
                         created_at DATETIME,

                         FOREIGN KEY (company_id) REFERENCES company(id),
                         FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- =========================
-- AVAILABILITY
-- =========================
CREATE TABLE availability (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              company_id BIGINT,
                              supplier_id BIGINT,
                              start_date DATE,
                              end_date DATE,
                              day_of_week VARCHAR(20),
                              start_time TIME,
                              end_time TIME,
                              recurrence_type VARCHAR(20),
                              slot_duration_minutes INT,
                              capacity INT,
                              status VARCHAR(20),
                              notes TEXT,
                              created_at DATETIME,

                              FOREIGN KEY (company_id) REFERENCES company(id),
                              FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- =========================
-- BOOKING
-- =========================
CREATE TABLE booking (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         company_id BIGINT,
                         service_id BIGINT,
                         customer_id BIGINT,
                         created_by_user_id BIGINT,
                         start_time DATETIME,
                         end_time DATETIME,
                         status VARCHAR(20),
                         type VARCHAR(20),
                         name VARCHAR(255),
                         description TEXT,
                         created_at DATETIME,

                         FOREIGN KEY (company_id) REFERENCES company(id),
                         FOREIGN KEY (service_id) REFERENCES service(id),
                         FOREIGN KEY (customer_id) REFERENCES customer(id),
                         FOREIGN KEY (created_by_user_id) REFERENCES user(id)
);

-- =========================
-- PAYMENT
-- =========================
CREATE TABLE payment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         company_id BIGINT,
                         booking_id BIGINT,
                         amount DECIMAL(10,2),
                         status VARCHAR(20),
                         method VARCHAR(20),
                         created_at DATETIME,

                         FOREIGN KEY (company_id) REFERENCES company(id),
                         FOREIGN KEY (booking_id) REFERENCES booking(id)
);

-- =========================
-- NOTIFICATION
-- =========================
CREATE TABLE notification (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT,
                              type VARCHAR(20),
                              message TEXT,
                              status VARCHAR(20),
                              target VARCHAR(255),
                              created_at DATETIME,
                              FOREIGN KEY (user_id) REFERENCES user(id)
);

-- =========================
-- CONFIGURATION
-- =========================
CREATE TABLE configuration (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT,

                               notify_appointments BOOLEAN,
                               notify_reservations BOOLEAN,
                               notify_cancellations BOOLEAN,
                               notify_reminders BOOLEAN,

                               reminder_minutes_before INT,
                               app_channel_enabled BOOLEAN,
                               email_channel_enabled BOOLEAN,

                               startTimeSilence TIME,
                               endTimeSilence TIME,
                               darkMode BOOLEAN,

                               FOREIGN KEY (user_id) REFERENCES user(id)
);