package com.babakalizada.user.service;

import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IUserService {
    public List<DtoUserResponse> getAllUsers();
    public DtoUserResponse getUsersByUsername(String username);
    public DtoUserResponse getUserById(Long id);
    public boolean deleteUserById(Long id);
    public boolean updateUser(Long id, DtoUpdateUserRequest dtoUserRequest);
}
