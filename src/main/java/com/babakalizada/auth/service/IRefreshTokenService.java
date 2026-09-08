package com.babakalizada.auth.service;

import com.babakalizada.auth.dto.request.DtoRefreshTokenRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;

public interface IRefreshTokenService {
    DtoLoginResponse refreshToken(DtoRefreshTokenRequest refreshToken);
}
