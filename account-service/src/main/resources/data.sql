INSERT INTO accounts ( id, user_id, account_number, branch_id, type_id, balance, withdrawal_limit, is_verified, is_active, is_locked, is_deleted,created_at,updated_at) 
SELECT 1,'9999999999','123456',101,1,10000,500000,true,true,false,false,'2021-01-01 00:00:00','2021-01-01 00:00:00' 
WHERE NOT EXISTS (SELECT * FROM accounts WHERE user_id = '9999999999');

INSERT INTO accounts ( id, user_id, account_number, branch_id, type_id, balance, withdrawal_limit, is_verified, is_active, is_locked, is_deleted,created_at,updated_at) 
SELECT 2,'88888888','654321',102,2,50000,500000,true,true,false,false,'2021-01-01 00:00:00','2021-01-01 00:00:00' 
WHERE NOT EXISTS (SELECT * FROM accounts WHERE user_id = '88888888');

-- ///////////////////////////////////////////////////////////////////////////////////////////////////

INSERT INTO Branch_Meta ( id, name, address, ifsc_code) 
SELECT 1,'Colaba','Mumbai','1A2B3C' 
WHERE NOT EXISTS (SELECT * FROM Branch_Meta WHERE id = 1);

INSERT INTO Branch_Meta ( id, name, address, ifsc_code) 
SELECT 2,'Kalyan','Thane','9A9B9C' 
WHERE NOT EXISTS (SELECT * FROM Branch_Meta WHERE id = 2);

-- ///////////////////////////////////////////////////////////////////////////////////////////////////

INSERT INTO Account_Type_Meta ( id, name) 
SELECT 1,'Loan' 
WHERE NOT EXISTS (SELECT * FROM Account_Type_Meta WHERE id = 1);

INSERT INTO Account_Type_Meta ( id, name) 
SELECT 2,'Savings' 
WHERE NOT EXISTS (SELECT * FROM Account_Type_Meta WHERE id = 2);

-- ///////////////////////////////////////////////////////////////////////////////////////////////////

INSERT INTO beneficiary_meta ( id, name, account_id, reciever_id, ifsc_code) 
SELECT 1,'Vir','999999','888888','1A2B3C' 
WHERE NOT EXISTS (SELECT * FROM beneficiary_meta WHERE id = 1);


INSERT INTO beneficiary_meta ( id, name, account_id, reciever_id, ifsc_code) 
SELECT 2,'Rao','111111','222222','9A9B9C' 
WHERE NOT EXISTS (SELECT * FROM beneficiary_meta WHERE id = 2);



    
    