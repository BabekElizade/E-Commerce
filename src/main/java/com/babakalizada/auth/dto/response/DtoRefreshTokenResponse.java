package com.babakalizada.auth.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoRefreshTokenResponse {
    private Long id;
    private String token;
    private Date expiredDate;
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;


}
