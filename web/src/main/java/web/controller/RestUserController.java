package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.security.service.UserService;

/**
 * Контроллер для обработки запросов, связанных с пользователями через REST API.
 */
@RestController
@RequestMapping("${api.path}/users")
public class RestUserController {
    private final UserService userService;

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод для установки роли администратора для пользователя.
     * @param username логин пользователя
     * @return строку с сообщением об успешной установке роли администратора
     */
    @GetMapping("/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String setRoleAdmin(@PathVariable String username) {
        userService.setAdminRole(username);
        return "The admin role is set to " + username;
    }
}
