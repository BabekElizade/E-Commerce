package com.babakalizada.auth.service.impl;

import com.babakalizada.auth.repository.IRefreshTokenRepository;
import com.babakalizada.auth.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshTokenServiceImpl implements IRefreshTokenService {
    private final IRefreshTokenRepository refreshTokenRepository;
}
