package com.babakalizada.auth.dto.response;

import com.babakalizada.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoLoginResponse {
    private String access_token;
    private DtoRefreshTokenResponse refresh_token;
    private Date expiredDate;
}
