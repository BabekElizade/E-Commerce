package com.babakalizada.auth.controller;

import com.babakalizada.auth.dto.request.DtoLoginRequest;
import com.babakalizada.auth.dto.request.DtoRefreshTokenRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.request.DtoRegisterRequest;
import com.babakalizada.auth.dto.response.DtoRegisterResponse;

public interface IRestAuthController {
    DtoRegisterResponse register(DtoRegisterRequest dtoRegisterRequest);

    DtoLoginResponse login(DtoLoginRequest dtoLoginRequest);

    DtoLoginResponse refreshToken(DtoRefreshTokenRequest refreshToken);
}
