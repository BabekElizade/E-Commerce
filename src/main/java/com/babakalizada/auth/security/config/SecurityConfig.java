package com.babakalizada.auth.security.config;

import com.babakalizada.auth.security.jwt.AuthEntryPoint;
import com.babakalizada.auth.security.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String[] PUBLIC_AUTH_PATHS = {
            "/login",
            "/register",
            "/refresh_token"
    };

    public static final String[] PUBLIC_GET_PATHS = {
            "/product/search",
            "/product/list",
            "/product/list/*",
            "/product/get-by-category/*",

            "/e-commerce/parent-category",
            "/e-commerce/category/*",

            "/review/get-by-product-id/*",
            "/review/*"
    };

    public static final String[] ADMIN_POST_PATHS = {
            "/product/create",
            "/e-commerce/create"
    };

    public static final String[] ADMIN_PUT_PATHS = {
            "/product/update/*",
            "/product/change-status/*",
            "/e-commerce/update-category/*"
    };

    public static final String[] ADMIN_DELETE_PATHS = {
            "/product/delete/*",
            "/e-commerce/delete/*"
    };

    public static final String[] ADMIN_GET_PATHS = {
            "/user/list",
    };

    public static final String[] AUTHENTICATED_PATHS = {
            "/user/by-id/*",
            "/user/by-username/*",
            "/user/update/*",
            "/user/delete/*",

            "/cart/**",
            "/wishlist/**",
            "/wallet/me/**",

            "/review/create",
            "/review/update/*",
            "/review/delete/*",
            "/review/get-my-reviews",

            "/payment/pay/*",
            "/payment/by-order/*",
            "/payment/by-order",
            "/orders/**"
    };

    public static final String[] SWAGGER_PATHS = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthEntryPoint authEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/*.html", "/admin/*.html", "/assets/**").permitAll()
                        .requestMatchers(HttpMethod.HEAD, "/", "/*.html", "/admin/*.html", "/assets/**").permitAll()

                        .requestMatchers(HttpMethod.POST, PUBLIC_AUTH_PATHS).permitAll()

                        .requestMatchers(HttpMethod.POST, ADMIN_POST_PATHS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_PUT_PATHS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_DELETE_PATHS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, ADMIN_GET_PATHS).hasRole("ADMIN")

                        .requestMatchers(AUTHENTICATED_PATHS).authenticated()

                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_PATHS).permitAll()

                        .anyRequest().denyAll()
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authEntryPoint)
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5500",
                "http://localhost:63342"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}