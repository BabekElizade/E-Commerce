package com.babakalizada.auth.service.impl;

import com.babakalizada.auth.dto.request.DtoLoginRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.response.DtoRefreshTokenResponse;
import com.babakalizada.auth.dto.response.DtoRegisterResponse;
import com.babakalizada.auth.dto.request.DtoRegisterRequest;
import com.babakalizada.auth.repository.IRefreshTokenRepository;
import com.babakalizada.auth.security.jwt.JWTService;
import com.babakalizada.auth.service.IAuthService;
import com.babakalizada.auth.entity.RefreshToken;
import com.babakalizada.user.enums.UserRole;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthService implements IAuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IUserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JWTService jwtService;
    private final IRefreshTokenRepository refreshTokenRepository;

    @Override
    public DtoRegisterResponse register(DtoRegisterRequest request) {
        User user = new User();
        DtoRegisterResponse dtoRegisterResponse = new DtoRegisterResponse();
        if (request.getNewPassword().equals(request.getConfirmPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            userRepository.save(user);
            BeanUtils.copyProperties(user, dtoRegisterResponse);
            return dtoRegisterResponse;
        }
        return null;
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 3600 * 1000 * 4));
        refreshToken.setUser(user);
        return refreshToken;
    }

    @Override
    public DtoLoginResponse login(DtoLoginRequest dtoLoginRequest) {
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(dtoLoginRequest.getUsername(), dtoLoginRequest.getPassword());
            authenticationProvider.authenticate(auth);
            Optional<User> user = userRepository.findUsersByUsername(dtoLoginRequest.getUsername());
            String accessToken = jwtService.generateToken(user.get());


            RefreshToken savedRefreshToken =
                    refreshTokenRepository.save(
                            createRefreshToken(user.get())
                    );

            DtoRefreshTokenResponse dtoRefreshTokenResponse = createDtoRefreshTokenResponse(savedRefreshToken);

            Date bakuZone = new Date(jwtService.getExpirationDateByToken(accessToken).getTime() + (4 * 60 * 60 * 1000));
            UserRole role = user.get().getRole();
            return new DtoLoginResponse(accessToken ,dtoRefreshTokenResponse ,bakuZone ,role);
        } catch (Exception e) {
            System.out.println("Username or Password is wrong!");
        }
        return null;
    }

    private static @NonNull DtoRefreshTokenResponse createDtoRefreshTokenResponse(RefreshToken savedRefreshToken) {
        DtoRefreshTokenResponse dtoRefreshTokenResponse = new DtoRefreshTokenResponse();
        dtoRefreshTokenResponse.setId(savedRefreshToken.getId());
        dtoRefreshTokenResponse.setToken(savedRefreshToken.getToken());
        dtoRefreshTokenResponse.setExpiredDate(savedRefreshToken.getExpiredDate());
        dtoRefreshTokenResponse.setUserId(savedRefreshToken.getUser().getId());
        dtoRefreshTokenResponse.setUsername(savedRefreshToken.getUser().getUsername());
        dtoRefreshTokenResponse.setFirstName(savedRefreshToken.getUser().getFirstName());
        dtoRefreshTokenResponse.setLastName(savedRefreshToken.getUser().getLastName());
        return dtoRefreshTokenResponse;
    }
}
