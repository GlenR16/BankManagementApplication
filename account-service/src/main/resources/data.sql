INSERT INTO accounts ( id, user_id, account_number, ifsc_code, type_id, balance, withdrawal_limit, is_verified, is_active, is_locked, is_deleted,created_at,updated_at) 
SELECT 1,'9999999999','123456','1A2B3C',1,10000,500000,true,true,false,false,'2021-01-01 00:00:00','2021-01-01 00:00:00' 
WHERE NOT EXISTS (SELECT * FROM accounts WHERE user_id = '9999999999');


INSERT INTO accounts ( id, user_id, account_number, ifsc_code, type_id, balance, withdrawal_limit, is_verified, is_active, is_locked, is_deleted,created_at,updated_at) 
SELECT 2,'88888888','654321','999XYZ',2,50000,500000,true,true,false,false,'2021-01-01 00:00:00','2021-01-01 00:00:00' 
WHERE NOT EXISTS (SELECT * FROM accounts WHERE user_id = '88888888');
    
    