package com.babakalizada.auth.controller.impl;

import com.babakalizada.auth.controller.IRestAuthController;
import com.babakalizada.auth.dto.request.DtoLoginRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.request.DtoRegisterRequest;
import com.babakalizada.auth.dto.response.DtoRegisterResponse;
import com.babakalizada.auth.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RestAuthControllerImpl implements IRestAuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Override
    public DtoRegisterResponse register(
            @Valid @RequestBody DtoRegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Override
    public DtoLoginResponse login(
            @Valid @RequestBody DtoLoginRequest request
    ) {
        return authService.login(request);
    }
}