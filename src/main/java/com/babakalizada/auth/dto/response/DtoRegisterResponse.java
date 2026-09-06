package com.babakalizada.auth.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoRegisterResponse {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;
}
