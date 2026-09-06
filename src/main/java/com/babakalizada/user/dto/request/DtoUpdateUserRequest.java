package com.babakalizada.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUpdateUserRequest {

    private String firstName;

    private String lastName;

}
