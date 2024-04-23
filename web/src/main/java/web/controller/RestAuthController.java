package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.security.dto.JwtAuthenticationResponse;
import web.security.dto.SignInRequest;
import web.security.dto.SignUpRequest;
import web.security.service.AuthenticationService;

/**
 * Контроллер для обработки запросов аутентификации через REST API.
 */
@RestController
@RequestMapping("${api.path}/auth")
public class RestAuthController {
    private final AuthenticationService authenticationService;

    @Autowired
    public RestAuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Метод для регистрации нового пользователя.
     * @param request данные запроса на регистрацию
     * @return объект с JWT-токеном для успешной регистрации
     */
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    /**
     * Метод для входа пользователя в систему.
     * @param request данные запроса на вход
     * @return объект с JWT-токеном для успешного входа
     */
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
