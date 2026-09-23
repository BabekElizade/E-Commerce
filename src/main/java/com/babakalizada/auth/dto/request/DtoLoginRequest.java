package com.babakalizada.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "Confirm Password must be not empty!")
    private String password;
}
