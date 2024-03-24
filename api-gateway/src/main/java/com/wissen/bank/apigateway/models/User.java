package com.wissen.bank.apigateway.models;

import java.util.Date;

public record User(long id,String customerId,String name,String email,String password,String role,String phone,String gender,String pan,String aadhar,String state,String city,String address,int pincode,Date date_of_birth,boolean is_locked,boolean is_deleted,Date created_at,Date updated_at){

}
