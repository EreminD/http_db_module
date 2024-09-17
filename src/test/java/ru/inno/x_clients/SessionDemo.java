package ru.inno.x_clients;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.inno.x_clients.jpa.entity.CompanyEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SessionDemo {

    // Hibernate dependency (POM)
    // Entity-классы
    // Configuration
    // Создать EntityManager <- EMFactory <- HibernatePersistenceProvider <- PUI

    private static Session session;

    @BeforeAll
    public static void setUp() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "env.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        Configuration configuration = new Configuration().setProperties(properties);
        configuration.addAnnotatedClass(CompanyEntity.class);

        session = configuration.buildSessionFactory().openSession();
        session.beginTransaction();
    }

    @AfterAll
    public static void tearDown() {
        session.close();
    }

    @Test
    public void iCanDelete() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Лютик");
        company.setDescription("Мощные бензопилы");
        company.setActive(true);

        session.persist(company);

        System.out.println(company);
    }
}
