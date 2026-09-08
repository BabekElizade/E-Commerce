package com.babakalizada.auth.security.jwt;

import com.babakalizada.common.constant.ErrorMessage;
import com.babakalizada.common.enums.ErrorCode;
import com.babakalizada.common.exception.InvalidTokenException;
import com.babakalizada.common.exception.TokenExpiredException;
import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final IUserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Autowired
    public JWTAuthenticationFilter(JWTService jwtService, UserDetailsService userDetailsService, IUserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();

        return path.endsWith("/register")
                || path.endsWith("/login")
                || path.endsWith("/refresh_token")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        final String token;
        final String username;

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = header.substring(7);
        try {
            username = jwtService.getUsernameByToken(token);
            Optional<User> user = userRepository.findUsersByUsername(username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userDetails != null) {
                        List<GrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_" + user.get().getRole())
                        );

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        authorities
                                );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
            }
        } catch (ExpiredJwtException e) {
            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    new TokenExpiredException(
                            new ErrorMessage(
                                    ErrorCode.TOKEN_EXPIRED,
                                    "Tokenin müddəti bitib. Yenidən login olun."
                            )
                    )
            );

            return;
        } catch (Exception e) {

            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    new InvalidTokenException(
                            new ErrorMessage(
                                    ErrorCode.INVALID_TOKEN,
                                    "Token etibarsızdır"
                            )
                    )
            );

            return;
        }
        filterChain.doFilter(request, response);
    }
}