package com.babakalizada.auth.service.impl;

import com.babakalizada.auth.dto.request.DtoRefreshTokenRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.response.DtoRefreshTokenResponse;
import com.babakalizada.auth.entity.RefreshToken;
import com.babakalizada.auth.repository.IRefreshTokenRepository;
import com.babakalizada.auth.security.jwt.JWTService;
import com.babakalizada.auth.service.IRefreshTokenService;
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.common.exception.TokenExpiredException;
import com.babakalizada.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;
    private final AuthService authService;
    private IUserRepository userRepository;

    public boolean isRefreshTokenExpired(Date expiredDate){
        return new Date().after(expiredDate);
    }

    @Override
    public DtoLoginResponse refreshToken(DtoRefreshTokenRequest refreshToken) {
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken.getRefreshToken());
        if(token.isEmpty()) {
            throw new ResourceNotFoundException(new ErrorMessage(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Refresh Token is not found!"
            ));
        }
        if(isRefreshTokenExpired(token.get().getExpiredDate())) {
            throw new TokenExpiredException(new  ErrorMessage(
               ErrorCode.TOKEN_EXPIRED,
               "Token has expired!"
            ));
        }

        String newAccessToken = jwtService.generateToken(token.get().getUser());
        RefreshToken newRefreshToken = authService.createRefreshToken(token.get().getUser());
        refreshTokenRepository.save(newRefreshToken);
        DtoRefreshTokenResponse newRefreshTokenResponse = new DtoRefreshTokenResponse();
        newRefreshTokenResponse.setId(newRefreshToken.getId());
        newRefreshTokenResponse.setToken(newRefreshToken.getToken());
        newRefreshTokenResponse.setExpiredDate(newRefreshToken.getExpiredDate());
        newRefreshTokenResponse.setUserId(newRefreshToken.getUser().getId());
        newRefreshTokenResponse.setUsername(newRefreshToken.getUser().getUsername());
        newRefreshTokenResponse.setFirstName(newRefreshToken.getUser().getFirstName());
        newRefreshTokenResponse.setLastName(newRefreshToken.getUser().getLastName());

        return new DtoLoginResponse(newAccessToken, newRefreshTokenResponse, newRefreshToken.getExpiredDate(), token.get().getUser().getRole());
    }
}
