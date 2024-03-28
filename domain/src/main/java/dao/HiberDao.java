package dao;

import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;
import essence.Identifiable;

import java.sql.SQLException;
import java.util.List;

@Component
public class HiberDao implements IDao {
    @ConfigProperty(propertyName = "db_url")
    private String dbUrl;
    @ConfigProperty(propertyName = "db_user")
    private String dbUser;
    @ConfigProperty(propertyName = "db_password")
    private String dbPassword;

    @Override
    public <T extends Identifiable> void save(T essence) throws SQLException {
    }

    @Override
    public <T extends Identifiable> void update(T essence) throws SQLException {
    }

    @Override
    public <T extends Identifiable> void delete(T essence) throws SQLException {
    }

    @Override
    public <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException {
        return null;
    }

    @Override
    public <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException {
        return null;
    }
}
