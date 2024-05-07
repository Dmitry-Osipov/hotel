package config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.file.ResponseEntityPreparer;

@Configuration
public class TestConfig {
    @Bean
    @Scope("prototype")
    public RoomService roomService() {
        return Mockito.mock(RoomService.class);
    }

    @Bean
    @Scope("prototype")
    public ServiceService serviceService() {
        return Mockito.mock(ServiceService.class);
    }

    @Bean
    @Scope("prototype")
    public ClientService clientService() {
        return Mockito.mock(ClientService.class);
    }

    @Bean
    @Scope("prototype")
    public ResponseEntityPreparer responseEntityPreparer() {
        return Mockito.mock(ResponseEntityPreparer.class);
    }
}
