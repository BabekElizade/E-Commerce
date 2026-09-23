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
import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.DuplicateAccountException;
import com.babakalizada.common.exception.ForbiddenException;
import com.babakalizada.common.exception.NullRequestException;
import com.babakalizada.common.exception.PasswordMatchException;
import com.babakalizada.user.enums.UserRole;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.enums.UserStatus;
import com.babakalizada.user.repository.IUserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public DtoRegisterResponse register(DtoRegisterRequest request) {
        DtoRegisterResponse dtoRegisterResponse = new DtoRegisterResponse();
        if(request == null) {
            throw new NullRequestException(
                    new ErrorMessage(
                            ErrorCode.NULL_REQUEST,
                            "Null Request"
                    )
            );
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMatchException(
                    new ErrorMessage(
                            ErrorCode.PASSWORD_MISMATCH,
                            "New Password and Confirm Password Do Not Match"
                    )
            );
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getNewPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(UserRole.USER)
                .status(UserStatus.PENDING)
                .build();

            if(userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateAccountException(
                        new ErrorMessage(
                                ErrorCode.DUPLICATE_ACCOUNT,
                                "Username or Email Already Exist"
                        )
                );
            }

        userRepository.save(user);
        BeanUtils.copyProperties(user, dtoRegisterResponse);
        return dtoRegisterResponse;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 3600 * 1000 * 4));
        refreshToken.setUser(user);
        return refreshToken;
    }

    @Transactional
    @Override
    public DtoLoginResponse login(DtoLoginRequest dtoLoginRequest) {

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(dtoLoginRequest.getUsername(), dtoLoginRequest.getPassword());
            authenticationProvider.authenticate(auth);
            Optional<User> user = userRepository.findUsersByUsername(dtoLoginRequest.getUsername());
            if (user.get().getStatus() != UserStatus.ACTIVE) {
                throw new ForbiddenException(
                        new ErrorMessage(
                                ErrorCode.FORBIDDEN,
                                "Account is not active"
                        )
                );
            }
            String accessToken = jwtService.generateToken(user.get());

            RefreshToken savedRefreshToken =
                    refreshTokenRepository.save(
                            createRefreshToken(user.get())
                    );

            DtoRefreshTokenResponse dtoRefreshTokenResponse = createDtoRefreshTokenResponse(savedRefreshToken);

        Date expiresAt = jwtService.getExpirationDateByToken(accessToken);
        UserRole role = user.get().getRole();
        return new DtoLoginResponse(accessToken ,dtoRefreshTokenResponse ,expiresAt);
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
