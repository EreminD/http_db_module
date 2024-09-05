package ru.inno.x_clients;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.inno.x_clients.jpa.entity.CompanyEntity;
import ru.inno.x_clients.jpa.manager.MyPUI;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class JPADemo {

    // Hibernate dependency (POM)
    // Entity-классы
    // PersistenceUnitInfo (меняем названия Entity-классов)
    // Создать EntityManager <- EMFactory <- HibernatePersistenceProvider <- PUI

    private static EntityManager entityManager;

    @BeforeAll
    public static void setUp() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "env.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        PersistenceUnitInfo pui = new MyPUI(properties);

        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory emf = hibernatePersistenceProvider.createContainerEntityManagerFactory(pui, pui.getProperties());
        entityManager = emf.createEntityManager();
    }

    @Test
    public void iCanGetCompany() {
        CompanyEntity company = entityManager.find(CompanyEntity.class, 5773);
        assertEquals(5773, company.getId());
        assertEquals("Muller LLC", company.getName());
        assertTrue(company.isActive());
    }

    @Test
    public void iCanGetCompanyByName() {
        String name = "Muller LLC";

        TypedQuery<CompanyEntity> query = entityManager.createQuery("SELECT ce FROM CompanyEntity ce WHERE ce.name = :desiredName", CompanyEntity.class);
        query.setParameter("desiredName", name);

        CompanyEntity singleResult = query.getSingleResult();


        assertEquals(5773, singleResult.getId());
        assertTrue(singleResult.isActive());
    }

    @Test
    public void iCanCreateANewCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Лютик");
        company.setDescription("Мощные бензопилы");
        company.setActive(true);

        entityManager.getTransaction().begin();
        entityManager.persist(company);
        entityManager.getTransaction().commit();

        System.out.println(company);
    }

    @Test
    public void iCanDelete() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Лютик");
        company.setDescription("Мощные бензопилы");
        company.setActive(true);

        entityManager.getTransaction().begin();
        entityManager.persist(company);
        entityManager.getTransaction().commit();

        System.out.println(company);

        int id = company.getId();

        entityManager.getTransaction().begin();
        entityManager.remove(company);
        entityManager.getTransaction().commit();

        CompanyEntity shouldBeNull = entityManager.find(CompanyEntity.class, id);
        assertNull(shouldBeNull);
    }
}


//   Такой вопрос: мы в условии from прописываем сущность, а не название таблицы.
//   Если у нас есть 2 таблицы с разным названием, в которые входят поля перечисленные в нашей сущности, какую он возьмет?