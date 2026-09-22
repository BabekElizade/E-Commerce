package com.babakalizada.user.controller;

import org.springframework.data.domain.Page;
import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;


public interface IRestUserController {
    public Page<DtoUserResponse> getAllUsers(int page, int size);

    public DtoUserResponse getUsersByUsername(String username);

    public DtoUserResponse getUserById(Long id);

    public boolean deleteUserById(Long id);

    public boolean updateUser(Long id, DtoUpdateUserRequest dtoUserRequest);
}
