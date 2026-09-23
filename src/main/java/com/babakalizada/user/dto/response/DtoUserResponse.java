package com.babakalizada.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUserResponse {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

}