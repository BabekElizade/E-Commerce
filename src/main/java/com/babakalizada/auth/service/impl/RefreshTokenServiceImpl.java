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
import com.babakalizada.common.exception.BusinessException;
import com.babakalizada.common.exception.ForbiddenException;
import com.babakalizada.common.exception.ResourceNotFoundException;
import com.babakalizada.common.exception.TokenExpiredException;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.enums.UserStatus;
import com.babakalizada.user.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;
    private final AuthService authService;
    private final IUserRepository userRepository;

    public boolean isRefreshTokenExpired(Date expiredDate){
        return new Date().after(expiredDate);
    }

    @Transactional
    @Override
    public DtoLoginResponse refreshToken(DtoRefreshTokenRequest refreshToken) {
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken.getRefreshToken());
        if(token.isEmpty()) {
            throw new ResourceNotFoundException(new ErrorMessage(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Refresh Token is not found!"
            ));
        }
        User currentUser = token.get().getUser();
        if(isRefreshTokenExpired(token.get().getExpiredDate())) {
            throw new TokenExpiredException(new  ErrorMessage(
               ErrorCode.TOKEN_EXPIRED,
               "Token has expired!"
            ));
        }
        if(currentUser.getStatus() != UserStatus.ACTIVE){
            throw new ForbiddenException(
                    new ErrorMessage(
                            ErrorCode.ACTIVATION_STATUS_NOT_MATCH,
                            "Current User is not Active!"
                    )
            );
        }
        String newAccessToken = jwtService.generateToken(currentUser);
        RefreshToken newRefreshToken = authService.createRefreshToken(currentUser);
        refreshTokenRepository.save(newRefreshToken);
        refreshTokenRepository.delete(token.get());
        DtoRefreshTokenResponse newRefreshTokenResponse = new DtoRefreshTokenResponse();
        newRefreshTokenResponse.setId(newRefreshToken.getId());
        newRefreshTokenResponse.setToken(newRefreshToken.getToken());
        newRefreshTokenResponse.setExpiredDate(newRefreshToken.getExpiredDate());
        newRefreshTokenResponse.setUserId(newRefreshToken.getUser().getId());
        newRefreshTokenResponse.setUsername(newRefreshToken.getUser().getUsername());
        newRefreshTokenResponse.setFirstName(newRefreshToken.getUser().getFirstName());
        newRefreshTokenResponse.setLastName(newRefreshToken.getUser().getLastName());

        return new DtoLoginResponse(
                newAccessToken,
                newRefreshTokenResponse,
                jwtService.getExpirationDateByToken(newAccessToken)
        );
    }
}
