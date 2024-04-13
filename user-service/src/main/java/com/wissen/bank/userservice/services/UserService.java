package com.wissen.bank.userservice.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.userservice.dto.UserDto;
import com.wissen.bank.userservice.models.Role;
import com.wissen.bank.userservice.models.User;
import com.wissen.bank.userservice.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user){
        if (user == null || !validateUser(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User details incomplete.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with this email already exists.");
        }
        if (userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with this phone number already exists.");
        }
        if (userRepository.findByPan(user.getPan()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with this PAN already exists.");
        }
        if (userRepository.findByAadhaar(user.getAadhaar()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with this Aadhaar already exists.");
        }
        String customerId = UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 10);
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
        return userRepository.save(_user);
    }

    @Transactional
    public UserDto createAdmin(User user){
        if (user == null || !validateUser(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User details incomplete.");
        }
        User _user = User
        .builder()
        .customerId(user.getCustomerId())
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
        .role(Role.ADMIN)
        .isLocked(false)
        .isDeleted(false)
        .build();
        _user = userRepository.save(_user);
        return UserDto.builder().customerId(_user.getCustomerId()).name(_user.getName()).email(_user.getEmail()).role(_user.getRole()).phone(_user.getPhone()).aadhaar(_user.getAadhaar()).pan(_user.getPan()).state(_user.getState()).city(_user.getCity()).address(_user.getAddress()).pincode(_user.getPincode()).dateOfBirth(_user.getDateOfBirth()).isLocked(_user.isLocked()).isDeleted(_user.isDeleted()).createdAt(_user.getCreatedAt()).updatedAt(_user.getUpdatedAt()).build();
    }

    public User getUserByCustomerId(String customerId){
        return userRepository.findByCustomerId(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
    }

    public boolean validateUser(User user){
        if (user.getName().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank() ||user.getPhone().isBlank() ||user.getPan().isBlank() || user.getAadhaar().isBlank() ||user.getState().isBlank() ||user.getCity().isBlank() ||user.getAddress().isBlank() ||user.getPincode() < 100000 ||user.getPincode() > 999999 ||user.getDateOfBirth().after(new Date())){
            return false;
        }
        return true;
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).gender(user.getGender()).build()).toList();
    }

    @Transactional
    public UserDto changePassword(String customerId, String oldPassword, String newPassword1, String newPassword2){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));;
        user.changePassword(oldPassword, newPassword1, newPassword2);
        user = userRepository.save(user);
        return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    @Transactional
    public UserDto updateUser(User newUser, String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));;
        if (user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()){
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()){
            user.setEmail(newUser.getEmail());
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
        user = userRepository.save(user);
        return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    @Transactional
    public UserDto deleteUserByCustomerId(String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        user.setDeleted(true);
        user = userRepository.save(user);
        return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    @Transactional
    public UserDto lockUser(String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        user.setLocked(true);
        user = userRepository.save(user);
        return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }

    @Transactional
    public UserDto unlockUser(String customerId){
        User user = userRepository.findByCustomerId(customerId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        user.setLocked(false);
        user = userRepository.save(user);
        return UserDto.builder().customerId(user.getCustomerId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).phone(user.getPhone()).aadhaar(user.getAadhaar()).pan(user.getPan()).state(user.getState()).city(user.getCity()).address(user.getAddress()).pincode(user.getPincode()).dateOfBirth(user.getDateOfBirth()).isLocked(user.isLocked()).isDeleted(user.isDeleted()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
    }
}
