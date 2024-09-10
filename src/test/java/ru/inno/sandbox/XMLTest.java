package ru.inno.sandbox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(ExceptionHandler.class)
public class XMLTest {

    public static final String URL = "https://www.w3schools.com/xml/cd_catalog.xml";


    @Test
    public void test(){

        String textToBe = "1988";
        given()
                .baseUri(URL)
                .when()
                .get()
                .then()
                .body(hasXPath("/CATALOG/CD[2]/YEAR[text()=\"1988\"]"));
    }

    @Test
    public void shouldThrowAnException(){
        throw new NullPointerException();
    }
}
