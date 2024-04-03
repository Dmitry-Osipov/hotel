package program;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages =
        {"ui", "ui.actions.room", "ui.actions.client", "ui.actions.service", "repository", "service", "dao"})
@PropertySource("classpath:config.properties")
public class AppConfig {
}
