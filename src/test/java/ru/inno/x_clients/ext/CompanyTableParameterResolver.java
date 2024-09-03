package ru.inno.x_clients.ext;

import org.junit.jupiter.api.extension.*;
import ru.inno.x_clients.db.CompanyRepository;
import ru.inno.x_clients.db.CompanyRepositoryJDBC;

import java.sql.Connection;
import java.sql.DriverManager;

public class CompanyTableParameterResolver implements ParameterResolver, AfterAllCallback, BeforeAllCallback {
    private final static String connectionString = "jdbc:postgresql://dpg-cqsr9ulumphs73c2q40g-a.frankfurt-postgres.render.com/x_clients_db_fxd0";
    private final static String username = "x_clients_user";
    private final static String password = "95PM5lQE0NfzJWDQmLjbZ45ewrz1fLYa";
    private Connection connection;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CompanyRepository.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new CompanyRepositoryJDBC(connection);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        connection = DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
