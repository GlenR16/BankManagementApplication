package com.wissen.bank.userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.userservice.dao.UserDao;
import com.wissen.bank.userservice.dao.UserPasswordDao;
import com.wissen.bank.userservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.userservice.exceptions.InvalidCredentialsException;
import com.wissen.bank.userservice.exceptions.TokenInvalidException;
import com.wissen.bank.userservice.exceptions.UnauthorizedException;
import com.wissen.bank.userservice.models.Role;
import com.wissen.bank.userservice.models.User;
import com.wissen.bank.userservice.responses.Response;
import com.wissen.bank.userservice.services.JWTService;
import com.wissen.bank.userservice.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<UserDao> getAllUsers(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin {} Getting all users",customerId);
            return userService.getAllUsers();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{customerId}")
    public UserDao getUserByCustomerId(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin Getting user id: {}",customerId);
            User user = userService.getUserByCustomerId(customerId);
            return UserDao.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).gender(user.getGender()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/details")
    public UserDao getUserDetails(@RequestHeader("Customer") String customerId) {
        User user =  userService.getUserByCustomerId(customerId);
        return UserDao.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).gender(user.getGender()).updatedAt(user.getUpdatedAt()).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> createUser(@RequestBody User user){
        User _user = userService.createUser(user);
        LOGGER.info("Creating user with customer id: {}",_user.getCustomerId());
        String token = jwtService.generateToken(_user);
        return ResponseEntity.ok().body(new Response(new Date(),200,token,"/user/signup"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody User user){
        User _user = userService.getUserByCustomerId(user.getCustomerId());
        if(!_user.isDeleted() && !_user.isLocked() && _user.verifyPassword(user.getPassword()) ){
            LOGGER.info("Logging in user id: {}",_user.getId());
            String token = jwtService.generateToken(_user);
            return ResponseEntity.ok().body(new Response(new Date(),200,token,"/user/login"));
        }
        LOGGER.error("Invalid credentials for user: {}",user.getCustomerId());
        throw new InvalidCredentialsException("Invalid Credentials");
    }

    @PostMapping("/verify")
    public UserDao verifyUser(@RequestBody Map<String,String> body){
        String customerId = jwtService.extractId(body.get("token"));
        User user = userService.getUserByCustomerId(customerId);
        return UserDao.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Response> deleteUser(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        LOGGER.info("Deleting user id: {}",customerId);
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            userService.deleteUserByCustomerId(customerId);
            return ResponseEntity.ok().body(new Response(new Date(),200,"User deleted successfully","/user/"+customerId));
        }
        else{
            throw new UnauthorizedException("Unauthorized");
        }
    }

    @PostMapping("/lock/{customerId}")
    public UserDao postLock(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        User user = userService.getUserByCustomerId(customerId);
        if (role == Role.ADMIN || role == Role.EMPLOYEE || user.getCustomerId().equals(customer)){
            LOGGER.info("Locking user id: {}",customerId);
            if (user.isLocked()){
                return userService.unlockUser(customerId);
            }
            else{
                return userService.lockUser(customerId);
            }
        }
        throw new UnauthorizedException("Unauthorized");
    }
    

    @PutMapping("/{customerId}")
    public ResponseEntity<Response> updateUser(@PathVariable String customerId, @RequestBody User user,@RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE || customer.equals(customerId)){
            LOGGER.info("Updating user id: {}",customerId);
            userService.updateUser(user, customerId);
            return ResponseEntity.ok().body(new Response(new Date(),200,"User updated successfully","/user/"+customerId));
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PutMapping("password")
    public UserDao putPassword(@RequestBody UserPasswordDao user ,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        return userService.changePassword(customerId, user.getOldPassword(), user.getNewPassword1(), user.getNewPassword2());
    }

    @PostConstruct
    public void init(){
        User user = User.builder()
            .customerId("9999999999")
            .password("password")
            .aadhaar("999999999999")
            .pan("AAAAA9999A")
            .email("admin@gmail.com")
            .address("123 / Kane Lane, Whitehouse")
            .name("Admin User")
            .city("DC")
            .state("Washington")
            .dateOfBirth(new Date())
            .gender("Male")
            .isDeleted(false)
            .isLocked(false)
            .phone("9999999999")
            .pincode(123456)
            .build();
        userService.createAdmin(user);
        User user2 = User.builder()
            .customerId("1111111111")
            .password("password")
            .aadhaar("123123123123")
            .pan("AAAAA1234A")
            .email("glen@gmail.com")
            .address("456 / Santacruz East")
            .name("Glen Rodrigues")
            .city("Mumbai")
            .state("Maharashtra")
            .pincode(123123)
            .dateOfBirth(new Date())
            .gender("Male")
            .isDeleted(false)
            .isLocked(false)
            .phone("1111111111")
            .build();
        userService.createUser(user2);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class, SQLIntegrityConstraintViolationException.class })
    public ResponseEntity<Response> handleSQLException(Exception e){
        LOGGER.error("Error: {}",e.getMessage());
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }

    @ExceptionHandler({ SignatureException.class, ExpiredJwtException.class })
    public ResponseEntity<Response> handleSignatureException(Exception e){
        LOGGER.error("Error: {}",e.getMessage());
        throw new TokenInvalidException("Token Invalid");
    }

    @ExceptionHandler({ NullPointerException.class })
    public ResponseEntity<Response> handleNullPointerException(Exception e){
        LOGGER.error("Error: {}",e.getMessage());
        throw new DatabaseIntegrityException("Some fields are empty");
    }
}
