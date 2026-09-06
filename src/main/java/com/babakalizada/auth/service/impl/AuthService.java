package com.babakalizada.auth.service.impl;

import com.babakalizada.auth.dto.request.DtoLoginRequest;
import com.babakalizada.auth.dto.response.DtoLoginResponse;
import com.babakalizada.auth.dto.response.DtoRegisterResponse;
import com.babakalizada.auth.dto.request.DtoRegisterRequest;
import com.babakalizada.auth.security.jwt.JWTService;
import com.babakalizada.auth.service.IAuthService;
import com.babakalizada.user.enums.UserRole;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthService implements IAuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IUserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JWTService jwtService;

    @Override
    public DtoRegisterResponse register(DtoRegisterRequest request) {
        User user = new User();
        DtoRegisterResponse dtoRegisterReponse = new DtoRegisterResponse();
        if(request.getNewPassword().equals(request.getConfirmPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            userRepository.save(user);
            BeanUtils.copyProperties(user,dtoRegisterReponse);
            return dtoRegisterReponse;
        }
        return null;
    }

    @Override
    public DtoLoginResponse login(DtoLoginRequest dtoLoginRequest) {
        try {
            UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(dtoLoginRequest.getUsername(), dtoLoginRequest.getPassword());
            authenticationProvider.authenticate(auth);
            Optional<User> user = userRepository.findUsersByUsername(dtoLoginRequest.getUsername());
            String token = jwtService.generateToken(user.get());
            Date bakuZone = new Date(jwtService.getExpirationDateByToken(token).getTime() + (4 * 60 * 60 * 1000));
            UserRole role = user.get().getRole();
            return new DtoLoginResponse(token, bakuZone, role);
        } catch (Exception e) {
            System.out.println("Username or Password is wrong!");
        }
        return null;
    }
}
