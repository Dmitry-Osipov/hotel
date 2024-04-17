package web.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Класс для представления запроса на вход (аутентификацию).
 */
@Data
public class SignInRequest {

    @Size(min = 5, max = 50, message = "The username must be between 5 and 50 characters long")
    @NotBlank(message = "The username cannot be blank")
    private String username;

    @Size(min = 8, max = 255, message = "The password length must be between 8 and 255 characters long")
    @NotBlank(message = "The password can't be blank")
    private String password;
}
