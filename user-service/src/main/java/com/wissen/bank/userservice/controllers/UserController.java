package com.wissen.bank.userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.userservice.models.Role;
import com.wissen.bank.userservice.models.User;
import com.wissen.bank.userservice.repositories.UserRepository;
import com.wissen.bank.userservice.services.JWTService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public String home(){
        return "Welcome to User Service";
    }
    

    @GetMapping("/all")
    public List<User> getAllUsers(){
        LOGGER.info("Getting all users");
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable long id){
        LOGGER.info("Getting user by id: {}",id);
        return userRepository.findById(id);
    }

    @PostMapping("/signup")
    public String createUser(@RequestBody User user){
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
        userRepository.save(_user);
        LOGGER.info("Creating user id: {}",_user.getId());
        return jwtService.generateToken(_user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user){
        Optional<User> _user = userRepository.findByCustomerId(user.getCustomerId());
        if(_user.isPresent()){
            if(_user.get().getPassword().equals(user.getPassword())){
                LOGGER.info("Logging in user id: {}",_user.get().getId());
                return jwtService.generateToken(_user.get());
            }
        }
        return "Invalid Credentials";
    }

    @PostMapping("/verify")
    public User verifyUser(@RequestBody Map<String,String> body){
        String customerId = null;
        try{
            customerId = jwtService.extractId(body.get("token"));
        }
        catch(Exception e){
            LOGGER.error("Error verifying user: {}",e.getMessage());
        }
        Optional<User> _user = userRepository.findByCustomerId(customerId);
        if(_user.isEmpty()){
            return null;
        }
        else{
            return _user.get();
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id){
        LOGGER.info("Deleting user id: {}",id);
        userRepository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user){
        Optional<User> _user = userRepository.findById(id);
        if (_user.isEmpty()){
            return null;
        }
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
        return userRepository.save(__user);
    }
    

    
}
