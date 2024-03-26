package com.wissen.bank.userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.userservice.models.Role;
import com.wissen.bank.userservice.models.User;
import com.wissen.bank.userservice.repositories.UserRepository;
import com.wissen.bank.userservice.responses.Response;
import com.wissen.bank.userservice.services.JWTService;

import io.jsonwebtoken.security.SignatureException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<User> getAllUsers(@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin Getting all users");
            return userRepository.findAll();
        }
        LOGGER.info("Getting user data of : {}",customerId);
        List<User> users = new ArrayList<User>();
        users.add(userRepository.findByCustomerId(customerId).get());
        return users;
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        if (role == Role.ADMIN || role == Role.EMPLOYEE){
            LOGGER.info("Admin Getting user id: {}",id);
            return userRepository.findById(id);
        }
        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> createUser(@RequestBody User user){
        String customerId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        User _user = User
        .builder()
        .customerId(customerId)
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .phone(user.getPhone())
        .aadhaar(user.getAadhaar())
        .pan(user.getPan())
        .address(user.getAddress())
        .state(user.getState())
        .city(user.getCity())
        .pincode(user.getPincode())
        .date_of_birth(user.getDate_of_birth())
        .gender(user.getGender())
        .role(Role.USER)
        .is_locked(false)
        .is_deleted(false)
        .build();
        LOGGER.info("Creating user id: {}",_user.getId());
        userRepository.save(_user);
        String token = jwtService.generateToken(_user);
        return ResponseEntity.ok().body(new Response(new Date(),200,token,"/user/signup"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody User user){
        Optional<User> _user = userRepository.findByCustomerId(user.getCustomerId());
        if(_user.isPresent()){
            if(!_user.get().is_deleted() && !_user.get().is_locked() && _user.get().getPassword().equals(user.getPassword())){
                LOGGER.info("Logging in user id: {}",_user.get().getId());
                String token = jwtService.generateToken(_user.get());
                return ResponseEntity.ok().body(new Response(new Date(),200,token,"/user/login"));
            }
        }
        LOGGER.error("Invalid credentials for user: {}",user.getCustomerId());
        return ResponseEntity.badRequest().body(new Response(new Date(),400,"Invalid credentials","/user/login"));
    }

    @PostMapping("/verify")
    public Optional<User> verifyUser(@RequestBody Map<String,String> body){
        String customerId = jwtService.extractId(body.get("token"));
        return userRepository.findByCustomerId(customerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        LOGGER.info("Deleting user id: {}",id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && (role == Role.ADMIN || role == Role.EMPLOYEE)){
            User _user = user.get();
            _user.set_deleted(true);
            userRepository.save(_user);
            return ResponseEntity.ok().body(new Response(new Date(),200,"User deleted successfully","/user/"+id));
        }
        return ResponseEntity.badRequest().body(new Response(new Date(),404,"Invalid details","/user/"+id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable long id, @RequestBody User user,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Optional<User> _user = userRepository.findById(id);
        if (_user.isEmpty()){
            return null;
        }
        if (role == Role.ADMIN || role == Role.EMPLOYEE || customerId == user.getCustomerId()){
            User __user = _user.get();
            __user.setName(user.getName());
            __user.setEmail(user.getEmail());
            __user.setPassword(user.getPassword());
            __user.setPhone(user.getPhone());
            __user.setAadhaar(user.getAadhaar());
            __user.setPan(user.getPan());
            __user.setAddress(user.getAddress());
            __user.setState(user.getState());
            __user.setCity(user.getCity());
            __user.setPincode(user.getPincode());
            __user.setDate_of_birth(user.getDate_of_birth());
            __user.setGender(user.getGender());
            __user.setUpdated_at(new Date());
            LOGGER.info("Updating user id: {}",__user.getId());
            userRepository.save(__user);
            return ResponseEntity.ok().body(new Response(new Date(),200,"User updated successfully","/user/"+id));
        }
        return ResponseEntity.badRequest().body(new Response(new Date(),404,"Invalid details","/user/"+id));
    }

    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Response> handleSQLException(Exception e){
        LOGGER.error("Error: {}",e.getMessage());
        return ResponseEntity.badRequest().body(new Response(new Date(),400,"Invalid data supplied","/user"));
    }

    @ExceptionHandler({ SignatureException.class })
    public ResponseEntity<Response> handleSignatureException(Exception e){
        LOGGER.error("Error: {}",e.getMessage());
        return ResponseEntity.badRequest().body(new Response(new Date(),400,"Invalid token","/user"));
    }
}
