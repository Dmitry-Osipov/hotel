package web.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utils.exceptions.EntityContainedException;
import web.security.entity.Role;
import web.security.entity.User;
import web.security.repository.UserRepository;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Сохранение пользователя
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создание пользователя
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new EntityContainedException("Username already exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new EntityContainedException("Email already exists");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Выдача прав администратора пользователю
     * @param username имя пользователя
     */
    public void setAdminRole(String username) {
        User user = getByUsername(username);
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
