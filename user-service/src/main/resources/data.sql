INSERT INTO users ( id, customer_id, name, email, password, role, phone, gender, pan, aadhaar, state, city, address, pincode, date_of_birth, is_locked, is_deleted, created_at, updated_at) 
SELECT 1,'9999999999','admin','admin@gmail.com','password',2,'1234567890','Male','1234567890','1234567890','Maharashtra','Mumbai','Mumbai',400001,'1990-01-01',false,false,'2021-01-01 00:00:00','2021-01-01 00:00:00' 
WHERE NOT EXISTS (SELECT * FROM users WHERE customer_id = '9999999999');
    