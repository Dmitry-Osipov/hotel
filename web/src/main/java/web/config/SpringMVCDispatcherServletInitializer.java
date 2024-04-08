package web.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Инициализатор диспетчера Spring MVC. Этот класс заменяет файл web.xml и настраивает DispatcherServlet.
 */
public class SpringMVCDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Возвращает конфигурационные классы для корневого контекста приложения. В данном случае не используется, поэтому
     * возвращается null.
     * @return массив конфигурационных классов
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    /**
     * Возвращает конфигурационные классы для Servlet контекста приложения. Здесь указывается класс {@link SpringConfig}
     * для настройки Spring MVC.
     * @return массив конфигурационных классов
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    /**
     * Возвращает маппинги для DispatcherServlet.
     * Здесь указывается, что DispatcherServlet будет обрабатывать все запросы, поступающие на корневой контекст
     * приложения ("/").
     * @return массив строк с маппингами
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement("/tmp", 2097152, 4194304, 0));
    }
}
