package web.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Инициализатор безопасности для веб-приложения. Этот класс расширяет {@link AbstractSecurityWebApplicationInitializer}
 * и автоматически регистрирует цепочку фильтров безопасности в контейнере сервлетов. Он инициализирует механизм
 * безопасности Spring Security, обеспечивая корректную работу аутентификации и авторизации в веб-приложении.
 */
public class WebSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
