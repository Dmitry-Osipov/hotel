package web.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Класс для представления запроса на регистрацию нового пользователя.
 */
@Data
public class SignUpRequest {

    @Size(min = 5, max = 50, message = "The username must be between 5 and 50 characters long")
    @NotBlank(message = "The username cannot be blank")
    private String username;

    @Size(min = 5, max = 255, message = "The email address must contain between 5 and 255 characters")
    @NotBlank(message = "The email address cannot be blank")
    @Email(message = "Email address should be in the format user@example.com")
    private String email;

    @Size(min = 8, max = 255, message = "Password length must be between 8 and 255 characters")
    private String password;
}
