package com.wissen.bank.userservice.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.bank.userservice.exceptions.NotFoundException;
import com.wissen.bank.userservice.models.Role;
import com.wissen.bank.userservice.models.User;
import com.wissen.bank.userservice.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        if (user == null || !validateUser(user)){
            throw new IllegalArgumentException("Invalid User");
        }
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
        .dateOfBirth(user.getDateOfBirth())
        .gender(user.getGender())
        .role(Role.USER)
        .isLocked(false)
        .isDeleted(false)
        .build();
        if (_user == null){
            throw new NotFoundException("User Not Found");
        }
        return userRepository.save(_user);
    }

    public User getUserByCustomerId(String customerId){
        return userRepository.findByCustomerId(customerId).orElseThrow(()-> new NotFoundException("User Not Found"));
    }

    public User getUserById(long id){
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
    }

    public boolean validateUser(User user){
        if (user.getName().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank() ||user.getPhone().isBlank() ||user.getPan().isBlank() || user.getAadhaar().isBlank() ||user.getState().isBlank() ||user.getCity().isBlank() ||user.getAddress().isBlank() ||user.getPincode() < 100000 ||user.getPincode() > 999999 ||user.getDateOfBirth().after(new Date())){
            return false;
        }
        return true;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(User newUser, String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new NotFoundException("User Not Found"));
        if (user == null){
            throw new NotFoundException("User Not Found");
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()){
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()){
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()){
            user.setPassword(newUser.getPassword());
        }
        if (newUser.getPhone() != null && !newUser.getPhone().isBlank()){
            user.setPhone(newUser.getPhone());
        }
        if (newUser.getGender() != null && !newUser.getGender().isBlank()){
            user.setGender(newUser.getGender());
        }
        if (newUser.getPan() != null && !newUser.getPan().isBlank()){
            user.setPan(newUser.getPan());
        }
        if (newUser.getAadhaar() != null && !newUser.getAadhaar().isBlank()){
            user.setAadhaar(newUser.getAadhaar());
        }
        if (newUser.getState() != null && !newUser.getState().isBlank()){
            user.setState(newUser.getState());
        }
        if (newUser.getCity() != null && !newUser.getCity().isBlank()){
            user.setCity(newUser.getCity());
        }
        if (newUser.getAddress() != null && !newUser.getAddress().isBlank()){
            user.setAddress(newUser.getAddress());
        }
        if (newUser.getPincode() > 100000 && newUser.getPincode() < 999999){
            user.setPincode(newUser.getPincode());
        }
        if (newUser.getDateOfBirth() != null){
            user.setDateOfBirth(newUser.getDateOfBirth());
        }
        if (!newUser.isLocked() && newUser.getUpdatedAt().before(DateUtils.addDays(new Date(), -2))){
            user.setLocked(newUser.isLocked());
        }
        if (newUser.isLocked()){
            user.setLocked(true);
        }
        return userRepository.save(user);
    }

    public User deleteUserByCustomerId(String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new NotFoundException("User Not Found"));
        user.setDeleted(true);
        return userRepository.save(user);
    }

    public User deleteUserById(long id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
        user.setDeleted(true);
        return userRepository.save(user);
    }

    public User lockUser(String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new NotFoundException("User Not Found"));
        user.setLocked(true);
        return userRepository.save(user);
    }

    public User unlockUser(String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new NotFoundException("User Not Found"));
        user.setLocked(false);
        return userRepository.save(user);
    }
}
