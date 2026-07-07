DROP DATABASE timora;
CREATE DATABASE IF NOT EXISTS timora;
USE timora;
-- =========================
-- COMPANY
-- =========================
CREATE TABLE company (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         ruc VARCHAR(50),
                         address VARCHAR(255),
                         phone VARCHAR(30),
                         email VARCHAR(255) NOT NULL UNIQUE,
                         status ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
                         created_at DATETIME NOT NULL
);
-- =========================
-- PERSON
-- =========================
CREATE TABLE person (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        company_id BIGINT NOT NULL,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        status ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
                        phone VARCHAR(30),
                        email VARCHAR(255) NOT NULL UNIQUE,
                        address VARCHAR(255),
                        created_at DATETIME NOT NULL,
                        FOREIGN KEY (company_id) REFERENCES company(id)
);
-- =========================
-- USER
-- =========================
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      company_id BIGINT NOT NULL,
                      person_id BIGINT NOT NULL UNIQUE,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password_hash VARCHAR(255) NOT NULL,
                      status ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
                      global_role ENUM('OWNER', 'ADMIN', 'USER') NOT NULL DEFAULT 'USER',
                      last_login_at DATETIME,
                      created_at DATETIME NOT NULL,
                      FOREIGN KEY (company_id) REFERENCES company(id),
                      FOREIGN KEY (person_id) REFERENCES person(id)
);
-- =========================
-- CUSTOMER
-- =========================
CREATE TABLE customer (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          company_id BIGINT NOT NULL,
                          person_id BIGINT NOT NULL UNIQUE,
                          notes TEXT,
                          created_at DATETIME NOT NULL,
                          FOREIGN KEY (company_id) REFERENCES company(id),
                          FOREIGN KEY (person_id) REFERENCES person(id)
);
-- =========================
-- SUPPLIER
-- =========================
CREATE TABLE supplier (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          company_id BIGINT NOT NULL,
                          person_id BIGINT NOT NULL UNIQUE,
                          specialty VARCHAR(255),
                          notes TEXT,
                          created_at DATETIME NOT NULL,
                          FOREIGN KEY (company_id) REFERENCES company(id),
                          FOREIGN KEY (person_id) REFERENCES person(id)
);
-- =========================
-- USER_SUPPLIER_PERMISSION
-- =========================
CREATE TABLE user_supplier_permission (
                                          user_id BIGINT NOT NULL,
                                          supplier_id BIGINT NOT NULL,
                                          permission ENUM(
        'BOOKING_CREATE', 'BOOKING_READ', 'BOOKING_UPDATE', 'BOOKING_DELETE',
        'ABILITY_CREATE', 'ABILITY_READ', 'ABILITY_UPDATE', 'ABILITY_DELETE',
        'SERVICE_CREATE', 'SERVICE_READ', 'SERVICE_UPDATE', 'SERVICE_DELETE',
        'CLIENT_CREATE', 'CLIENT_READ', 'CLIENT_UPDATE', 'CLIENT_DELETE'
    ) NOT NULL,
                                          assigned_by_user_id BIGINT,
                                          created_at DATETIME NOT NULL,
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
                         company_id BIGINT NOT NULL,
                         supplier_id BIGINT NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         duration INT NOT NULL,
                         status ENUM('ACTIVE', 'INACTIVE', 'TEMPORARILY_UNAVAILABLE', 'ARCHIVED', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
                         created_at DATETIME NOT NULL,
                         FOREIGN KEY (company_id) REFERENCES company(id),
                         FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);
-- =========================
-- AVAILABILITY
-- =========================
CREATE TABLE availability (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              company_id BIGINT NOT NULL,
                              supplier_id BIGINT NOT NULL,
                              start_date DATE,
                              end_date DATE,
                              day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'),
                              start_time TIME,
                              end_time TIME,
                              recurrence_type ENUM('NONE', 'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY', 'CUSTOM') DEFAULT 'NONE',
                              slot_duration_minutes INT,
                              capacity INT,
                              status ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
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
                         company_id BIGINT NOT NULL,
                         service_id BIGINT NOT NULL,
                         customer_id BIGINT NOT NULL,
                         created_by_user_id BIGINT NOT NULL,
                         start_time DATETIME NOT NULL,
                         end_time DATETIME NOT NULL,
                         status ENUM('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'PENDING',
                         type ENUM('APPOINTMENT', 'RESERVATION') NOT NULL DEFAULT 'APPOINTMENT',
                         name VARCHAR(255),
                         description TEXT,
                         created_at DATETIME NOT NULL,
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
                         company_id BIGINT NOT NULL,
                         booking_id BIGINT NOT NULL,
                         amount DECIMAL(10,2) NOT NULL,
                         status ENUM('PENDING', 'PAID', 'PARTIALLY_PAID', 'FAILED', 'REFUNDED', 'CANCELLED', 'DELETED') NOT NULL DEFAULT 'PENDING',
                         method ENUM('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'YAPE', 'PLIN', 'DIGITAL_WALLET', 'OTHER') NOT NULL DEFAULT 'CASH',
                         created_at DATETIME NOT NULL,
                         FOREIGN KEY (company_id) REFERENCES company(id),
                         FOREIGN KEY (booking_id) REFERENCES booking(id)
);
-- =========================
-- NOTIFICATION
-- =========================
CREATE TABLE notification (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              is_read BOOLEAN NOT NULL DEFAULT FALSE,
                              type ENUM('BOOKING', 'CANCELLATION', 'REMINDER', 'SYSTEM', 'PAYMENT') NOT NULL,
                              message TEXT NOT NULL,
                              status ENUM('PENDING', 'QUEUED', 'SENT', 'DELIVERED', 'READ', 'FAILED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
                              target VARCHAR(255),
                              created_at DATETIME NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES user(id)
);
-- =========================
-- CONFIGURATION
-- =========================
CREATE TABLE configuration (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL UNIQUE,
                               notify_appointments BOOLEAN DEFAULT TRUE,
                               notify_reservations BOOLEAN DEFAULT TRUE,
                               notify_cancellations BOOLEAN DEFAULT TRUE,
                               notify_reminders BOOLEAN DEFAULT TRUE,
                               reminder_minutes_before INT DEFAULT 30,
                               app_channel_enabled BOOLEAN DEFAULT TRUE,
                               email_channel_enabled BOOLEAN DEFAULT TRUE,
                               start_time_silence TIME DEFAULT '22:00:00',
                               end_time_silence TIME DEFAULT '07:00:00',
                               dark_mode BOOLEAN DEFAULT FALSE,
                               FOREIGN KEY (user_id) REFERENCES user(id)
);

