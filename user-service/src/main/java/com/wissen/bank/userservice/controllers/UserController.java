package com.wissen.bank.userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

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
    public List<User> getAllUsers(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin {} Getting all users",customerId);
            return userService.getAllUsers();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{customerId}")
    public User getUserById(@PathVariable String customerId, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin Getting user id: {}",customerId);
            return userService.getUserByCustomerId(customerId);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/details")
    public User getUserDetails(@RequestHeader("Customer") String customerId) {
        return userService.getUserByCustomerId(customerId);
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> createUser(@RequestBody User user){
        User _user = userService.createUser(user);
        LOGGER.info("Creating user id: {}",_user.getId());
        String token = jwtService.generateToken(_user);
        return ResponseEntity.ok().body(new Response(new Date(),200,token,"/user/signup"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody User user){
        User _user = userService.getUserByCustomerId(user.getCustomerId());
        if(!_user.isDeleted() && !_user.isLocked() && _user.getPassword().equals(user.getPassword())){
            LOGGER.info("Logging in user id: {}",_user.getId());
            String token = jwtService.generateToken(_user);
            return ResponseEntity.ok().body(new Response(new Date(),200,token,"/user/login"));
        }
        LOGGER.error("Invalid credentials for user: {}",user.getCustomerId());
        throw new InvalidCredentialsException("Invalid Credentials");
    }

    @PostMapping("/verify")
    public User verifyUser(@RequestBody Map<String,String> body){
        String customerId = jwtService.extractId(body.get("token"));
        return userService.getUserByCustomerId(customerId);
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

    @PutMapping("/{customerId}")
    public ResponseEntity<Response> updateUser(@PathVariable String customerId, @RequestBody User user,@RequestHeader("Customer") String customer, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE || customer.equals(customerId)){
            LOGGER.info("Updating user id: {}",customerId);
            userService.updateUser(user, customerId);
            return ResponseEntity.ok().body(new Response(new Date(),200,"User updated successfully","/user/"+customerId));
        }
        throw new UnauthorizedException("Unauthorized");
    }



    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class })
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
