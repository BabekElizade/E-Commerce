package com.babakalizada.user.controller;

import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;

import java.util.List;

public interface IRestUserController {
    public List<DtoUserResponse> getAllUsers();
    public DtoUserResponse getUsersByUsername(String username);
    public DtoUserResponse getUserById(Long id);
    public boolean deleteUserById(Long id);
    public boolean updateUser(Long id, DtoUpdateUserRequest dtoUserRequest);
}
