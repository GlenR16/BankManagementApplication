INSERT INTO cards ( id, account_id, number, cvv, pin, expiry_date, type_id, is_verified, is_active, is_locked, is_deleted,created_at,updated_at) 
SELECT 1,'9999999999',123456,665,5432,'2028-01-01 00:00:00',1,true,true,false,false,'2021-01-01 00:00:00','2021-01-01 00:00:00' 
WHERE NOT EXISTS (SELECT * FROM cards WHERE account_id = '9999999999');


-- ///////////////////////////////////////////////////////////////////////////////////////////////////

INSERT INTO Card_Type_Meta ( id, name, intrest) 
SELECT 1,'Rupay', 2500 
WHERE NOT EXISTS (SELECT * FROM Card_Type_Meta WHERE id = 1);

INSERT INTO Card_Type_Meta ( id, name, intrest) 
SELECT 2,'Visa', 3000 
WHERE NOT EXISTS (SELECT * FROM Card_Type_Meta WHERE id = 2);

INSERT INTO Card_Type_Meta ( id, name, intrest) 
SELECT 3,'Debit', 1000 
WHERE NOT EXISTS (SELECT * FROM Card_Type_Meta WHERE id = 3);