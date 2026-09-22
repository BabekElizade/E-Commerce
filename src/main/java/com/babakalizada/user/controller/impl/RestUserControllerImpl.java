package com.babakalizada.user.controller.impl;

import com.babakalizada.user.controller.IRestUserController;
import com.babakalizada.user.dto.request.DtoUpdateUserRequest;
import com.babakalizada.user.dto.response.DtoUserResponse;
import com.babakalizada.user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class RestUserControllerImpl implements IRestUserController {
    private final IUserService userService;

    @Autowired
    public RestUserControllerImpl(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/list")
    @Override
    public List<DtoUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "by-username/{username}")
    @Override
    public DtoUserResponse getUsersByUsername(@PathVariable(name = "username") String username) {
        return userService.getUsersByUsername(username);
    }

    @GetMapping(path = "/by-id/{id}")
    @Override
    public DtoUserResponse getUserById(@PathVariable(name = "id") Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping(path = "delete/{id}")
    @Override
    public boolean deleteUserById(@PathVariable(name = "id") Long id) {
        return userService.deleteUserById(id);
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public boolean updateUser(@PathVariable(name = "id") Long id, @Valid @RequestBody DtoUpdateUserRequest dtoUserRequest) {
        return userService.updateUser(id, dtoUserRequest);
    }
}
