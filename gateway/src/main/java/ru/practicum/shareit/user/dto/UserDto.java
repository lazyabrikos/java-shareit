package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Email(message = "Wrong format of email address")
    @NotBlank(message = "Email address cannot be empty")
    private String email;

    @NotBlank(message = "User name cannot be empty")
    private String name;
}
