package ru.inno.x_clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.x_clients.db.CompanyRepository;
import ru.inno.x_clients.ext.ApiHelperParameterResolver;
import ru.inno.x_clients.ext.CompanyTableParameterResolver;
import ru.inno.x_clients.ext.NewCompanyParameterResolver;
import ru.inno.x_clients.helper.CompanyApiHelper;
import ru.inno.x_clients.model.Company;
import ru.inno.x_clients.model.CompanyDB;
import ru.inno.x_clients.model.CreateCompanyResponse;

import java.sql.SQLException;
import java.util.Optional;

import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ApiHelperParameterResolver.class, NewCompanyParameterResolver.class, CompanyTableParameterResolver.class})
public class CompanyBusinessTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://x-clients-be.onrender.com"; // properties.getProperty("url");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        RestAssured.config = RestAssured.config().objectMapperConfig(objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper));
    }

    @Test
    public void iCanCreateCompany(CompanyApiHelper helper, CompanyRepository repository) throws SQLException {
        String name = "Inno";
        String descr = "Курсы";
        CreateCompanyResponse company = helper.createCompany(name, descr);
        int companyId = company.id();

        CompanyDB result = repository.selectById(companyId);

        assertEquals(companyId, result.id());
        assertEquals(name, result.name());
        assertEquals(descr, result.description());
        assertTrue(result.isActive());
    }

    @Test
    public void iCanReadCompanyInfo(CompanyApiHelper helper, CompanyRepository repository) throws SQLException, InterruptedException {
        String name = "Удалите меня";
        String descr = "Описание компании";
        int newCompanyId = repository.createCompany(name, descr);

        Optional<Company> byId = helper.getById(newCompanyId);

        assertTrue(byId.isPresent());
        assertEquals(descr, byId.get().description());
        assertEquals(name, byId.get().name());
    }

    @Test
    public void iCanDeleteCompany(CompanyApiHelper helper, CompanyRepository repository) throws InterruptedException, SQLException {
        int newCompanyId = repository.createCompany("Временная организация", "");
        helper.deleteCompany(newCompanyId);
        Optional<Company> optional = helper.getById(3376);
        assertTrue(optional.isEmpty());
    }

    //TODO: сверить множество записей. Например, получить неактивные компании и сравнить id'шники?
}

