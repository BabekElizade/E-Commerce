package com.babakalizada.auth.service.impl;

import com.babakalizada.auth.dto.request.DtoRefreshTokenRequest;
import com.babakalizada.auth.entity.RefreshToken;
import com.babakalizada.auth.repository.IRefreshTokenRepository;
import com.babakalizada.auth.security.jwt.JWTService;
import com.babakalizada.common.exception.ForbiddenException;
import com.babakalizada.common.exception.TokenExpiredException;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.enums.UserStatus;
import com.babakalizada.user.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {
    @Mock private IRefreshTokenRepository repository;
    @Mock private JWTService jwtService;
    @Mock private AuthService authService;
    @Mock private IUserRepository userRepository;

    private RefreshTokenServiceImpl service;
    private User user;
    private RefreshToken oldToken;
    private DtoRefreshTokenRequest request;

    @BeforeEach
    void setUp() {
        service = new RefreshTokenServiceImpl(repository, jwtService, authService, userRepository);
        user = User.builder().id(1L).username("test-user").status(UserStatus.ACTIVE).build();
        oldToken = RefreshToken.builder().id(10L).token("old-token")
                .expiredDate(new Date(System.currentTimeMillis() + 60_000)).user(user).build();
        request = DtoRefreshTokenRequest.builder().refreshToken("old-token").build();
        when(repository.findByToken("old-token")).thenReturn(Optional.of(oldToken));
    }

    @Test
    void rotatesValidTokenAndReturnsAccessTokenExpiry() {
        RefreshToken replacement = replacement();
        Date accessExpiry = new Date(System.currentTimeMillis() + 120_000);
        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(authService.createRefreshToken(user)).thenReturn(replacement);
        when(jwtService.getExpirationDateByToken("access-token")).thenReturn(accessExpiry);

        var response = service.refreshToken(request);

        assertEquals("access-token", response.getAccess_token());
        assertEquals("new-token", response.getRefresh_token().getToken());
        assertEquals(accessExpiry, response.getExpiredDate());
        assertEquals(replacement.getExpiredDate(), response.getRefresh_token().getExpiredDate());
        var order = inOrder(repository);
        order.verify(repository).findByToken("old-token");
        order.verify(repository).save(replacement);
        order.verify(repository).delete(oldToken);
        verifyNoInteractions(userRepository);
    }

    @Test
    void failedReplacementSaveDoesNotDeleteOldToken() {
        RefreshToken replacement = replacement();
        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(authService.createRefreshToken(user)).thenReturn(replacement);
        when(repository.save(replacement)).thenThrow(new IllegalStateException("Save failed"));

        assertThrows(IllegalStateException.class, () -> service.refreshToken(request));

        verify(repository, never()).delete(any(RefreshToken.class));
    }

    @Test
    void expiredTokenIsNotRotated() {
        oldToken.setExpiredDate(new Date(System.currentTimeMillis() - 60_000));

        assertThrows(TokenExpiredException.class, () -> service.refreshToken(request));

        verify(repository, never()).save(any(RefreshToken.class));
        verify(repository, never()).delete(any(RefreshToken.class));
        verifyNoInteractions(jwtService, authService);
    }

    @Test
    void inactiveAccountIsNotRotated() {
        user.setStatus(UserStatus.INACTIVE);

        assertThrows(ForbiddenException.class, () -> service.refreshToken(request));

        verify(repository, never()).save(any(RefreshToken.class));
        verify(repository, never()).delete(any(RefreshToken.class));
        verifyNoInteractions(jwtService, authService);
    }

    private RefreshToken replacement() {
        return RefreshToken.builder().id(11L).token("new-token")
                .expiredDate(new Date(System.currentTimeMillis() + 240_000)).user(user).build();
    }
}
