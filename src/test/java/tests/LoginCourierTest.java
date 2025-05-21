package tests;

import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import models.Courier;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class LoginCourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setup() {
        courierClient = new CourierClient();
        courier = new Courier("test_" + System.currentTimeMillis(), "password", "name");
        courierClient.create(courier);
        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");
    }

    @After
    public void teardown() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void testLoginSuccess() {
        Response response = courierClient.login(courier);
        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("$", hasKey("id")) // Проверка наличие поля
                .body("id", is(not(0))); // Проверка что id не нулевой
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void testLoginWithWrongPassword() {
        Courier courierWithWrongPassword = new Courier(courier.getLogin(), "wrongPassword", courier.getFirstName());
        Response response = courierClient.login(courierWithWrongPassword);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"))
                .body("$", hasKey("message")); // Проверка структуры месседжа с ошибкой
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void testLoginWithoutRequiredField() {
        Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());
        Response response = courierClient.login(courierWithoutPassword);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .body("$", hasKey("message")); // Проверка структуры месседжа с ошибкой
    }
}