package com.babakalizada.auth.service;

import com.babakalizada.auth.dto.request.DtoLoginRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.response.DtoRegisterResponse;
import com.babakalizada.auth.dto.request.DtoRegisterRequest;

public interface IAuthService {
    public DtoRegisterResponse register(DtoRegisterRequest request);
    public DtoLoginResponse login(DtoLoginRequest dtoLoginRequest);
}
