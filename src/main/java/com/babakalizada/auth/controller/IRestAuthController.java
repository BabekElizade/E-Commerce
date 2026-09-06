package com.babakalizada.auth.controller;

import com.babakalizada.auth.dto.request.DtoLoginRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.request.DtoRegisterRequest;
import com.babakalizada.auth.dto.response.DtoRegisterResponse;

public interface IRestAuthController {
    public DtoRegisterResponse register(DtoRegisterRequest dtoRegisterRequest);
    public DtoLoginResponse login(DtoLoginRequest dtoLoginRequest);
}
