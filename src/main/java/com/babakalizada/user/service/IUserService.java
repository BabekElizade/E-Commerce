package com.babakalizada.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;
import jakarta.validation.Valid;


public interface IUserService {
    public Page<DtoUserResponse> getAllUsers(Pageable pageable);

    public DtoUserResponse getUsersByUsername(String username);

    public DtoUserResponse getUserById(Long id);

    public boolean deleteUserById(Long id);

    public boolean updateUser(Long id, DtoUpdateUserRequest dtoUserRequest);
}
