package com.babakalizada.auth.service;

import com.babakalizada.auth.dto.request.DtoRefreshTokenRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.response.DtoRefreshTokenResponse;

public interface IRefreshTokenService {
    DtoLoginResponse refreshToken(DtoRefreshTokenRequest refreshToken);
}
