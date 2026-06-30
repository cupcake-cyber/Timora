USE timora;

INSERT INTO notification (user_id, type,is_read, message, status, target, created_at)
VALUES
    (1, 'SYSTEM',false, 'Welcome to Timora! Your account is ready.', 'DELIVERED', '/home', NOW());

use timora;
INSERT INTO service (company_id, supplier_id, name, description, price, duration, status, created_at)
VALUES (2, 1, 'Haircut & Style', 'Precision cut with blow-dry finish', 45.00, 60, 'ACTIVE', NOW());