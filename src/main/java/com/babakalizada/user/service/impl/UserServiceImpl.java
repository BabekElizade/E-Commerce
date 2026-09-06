package com.babakalizada.user.service.impl;

import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import com.babakalizada.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<DtoUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<DtoUserResponse> dtoUserResponseList = new ArrayList<>();
        for(User user : users) {
            DtoUserResponse dtoUserResponse = new DtoUserResponse();
            BeanUtils.copyProperties(user, dtoUserResponse);
            dtoUserResponseList.add(dtoUserResponse);
        }
        return dtoUserResponseList;
    }

    @Override
    public DtoUserResponse getUsersByUsername(String username) {
        Optional<User> users = userRepository.findUsersByUsername(username);
        if(users.isEmpty()) {
            return null;
        }
        DtoUserResponse dtoUserResponse = new DtoUserResponse();
        BeanUtils.copyProperties(users.get(), dtoUserResponse);
        return dtoUserResponse;
    }

    @Override
    public DtoUserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return null;
        }
        DtoUserResponse dtoUserResponse = new DtoUserResponse();
        BeanUtils.copyProperties(user.get(), dtoUserResponse);
        return dtoUserResponse;
    }

    @Override
    public boolean deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateUser(Long id, DtoUpdateUserRequest dtoUserRequest) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return false;
        }
        if(dtoUserRequest.getFirstName() != null && dtoUserRequest.getLastName().isEmpty()) {
            user.get().setFirstName(dtoUserRequest.getFirstName());
            userRepository.save(user.get());
            return true;
        } else if (dtoUserRequest.getFirstName() == null && !dtoUserRequest.getLastName().isEmpty()) {
            user.get().setLastName(dtoUserRequest.getLastName());
            userRepository.save(user.get());
            return true;
        } else if(dtoUserRequest.getFirstName() != null && dtoUserRequest.getLastName() != null) {
            user.get().setFirstName(dtoUserRequest.getFirstName());
            user.get().setLastName(dtoUserRequest.getLastName());
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
