package com.wissen.bank.userservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.userservice.dto.UserDto;
import com.wissen.bank.userservice.dto.UserPasswordDto;
import com.wissen.bank.userservice.models.Role;
import com.wissen.bank.userservice.models.User;
import com.wissen.bank.userservice.services.JWTService;
import com.wissen.bank.userservice.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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
    

    @GetMapping("")
    public List<UserDto> getAllUsers(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            return userService.getAllUsers();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/{customerId}")
    public UserDto getUserByCustomerId(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE || customer.equals(customerId)){
            User user = userService.getUserByCustomerId(customerId);
            return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).gender(user.getGender()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/details")
    public UserDto getUserDetails(@RequestHeader("Customer") String customerId) {
        User user =  userService.getUserByCustomerId(customerId);
        return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).gender(user.getGender()).updatedAt(user.getUpdatedAt()).build();
    }

    @PostMapping("/signup")
    public UserDto createUser(@RequestBody User user){
        User _user = userService.createUser(user);
        return UserDto.builder().customerId(_user.getCustomerId()).name(_user.getName()).email(_user.getEmail()).gender(_user.getGender()).role(_user.getRole()).phone(_user.getPhone()).aadhaar(_user.getAadhaar()).pan(_user.getPan()).state(_user.getState()).city(_user.getCity()).address(_user.getAddress()).pincode(_user.getPincode()).dateOfBirth(_user.getDateOfBirth()).isLocked(_user.isLocked()).isDeleted(_user.isDeleted()).createdAt(_user.getCreatedAt()).updatedAt(_user.getUpdatedAt()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user){
        User _user = userService.getUserByCustomerId(user.getCustomerId());
        if (_user.isDeleted() || _user.isLocked()) throw new ResponseStatusException(HttpStatus.LOCKED, "Account is locked or deleted.");
        if( _user.verifyPassword(user.getPassword()) ){
            String token = jwtService.generateToken(_user);
            return ResponseEntity.ok().body(token);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");
    }

    @PostMapping("/verify")
    public UserDto verifyUser(@RequestBody Map<String,String> body){
        String customerId = jwtService.extractId(body.get("token"));
        User user = userService.getUserByCustomerId(customerId);
        return UserDto.builder().customerId(user.getCustomerId()).gender(user.getGender()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    @DeleteMapping("/{customerId}")
    public UserDto deleteUser(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            User user = userService.getUserByCustomerId(customerId);
            userService.deleteUserByCustomerId(customerId);
            return UserDto.builder().customerId(user.getCustomerId()).gender(user.getGender()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot delete this account.");
    }

    @PostMapping("/lock/{customerId}")
    public UserDto postLock(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        User user = userService.getUserByCustomerId(customerId);
        if (role == Role.ADMIN || role == Role.EMPLOYEE || user.getCustomerId().equals(customer)){
            if (user.isLocked()){
                return userService.unlockUser(customerId);
            }
            else if (!user.isLocked() && user.getUpdatedAt().before(DateUtils.addDays(new Date(), -2))){
                return userService.lockUser(customerId);
            }
            else{
                throw new ResponseStatusException(HttpStatus.LOCKED,"Account cannot be unlocked before 2 days have passed.");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }
    

    @PutMapping("/{customerId}")
    public UserDto updateUser(@PathVariable String customerId, @RequestBody User user,@RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE || customer.equals(customerId)){
            return userService.updateUser(user, customerId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot edit these details.");
    }

    @PutMapping("password")
    public UserDto putPassword(@RequestBody UserPasswordDto user ,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        if (role == Role.USER){
            return userService.changePassword(customerId, user.getOldPassword(), user.getNewPassword1(), user.getNewPassword2());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin cannot edit these details.");
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
    public void handleSQLException(Exception e){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some fields are already used.");
    }

    @ExceptionHandler({ SignatureException.class, ExpiredJwtException.class, MalformedJwtException.class })
    public void handleSignatureException(Exception e){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is invalid or has expired.");
    }

    @ExceptionHandler({ NullPointerException.class })
    public void handleNullPointerException(Exception e){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found.");
    }
}
