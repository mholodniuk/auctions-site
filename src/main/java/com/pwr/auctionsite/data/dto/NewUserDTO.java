package com.pwr.auctionsite.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewUserDTO {
    @NotBlank(message = "username can not be blank")
    @Size(max = 32, message = "username can not exceed 32 characters")
    private String username;

    @NotBlank(message = "password can not be blank")
    @Size(min = 8, max = 32, message = "password size should be between 8 and 16")
    private String password;

    @Email
    @NotBlank(message = "email can not be blank")
    @Size(max = 32, message = "email can not exceed 32 characters")
    private String email;

    @NotBlank(message = "First name can not be blank")
    @Size(max = 32, message = "First name can not exceed 32 characters")
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    @Size(max = 32, message = "Last name can not exceed 32 characters")
    private String lastName;
}
