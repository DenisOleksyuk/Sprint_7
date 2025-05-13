package tests;

import clients.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import models.Courier;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setup() {
        courierClient = new CourierClient();
        courier = new Courier("test_" + System.currentTimeMillis(), "password", "name");
    }

    @After
    public void teardown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourierSuccess() {
        Response response = courierClient.create(courier);
        response.then()
                .statusCode(201)
                .body("ok", is(true))
                .body("$", hasKey("ok")); // Есть ли "ok" в ответе

        Response loginResponse = courierClient.login(courier);
        loginResponse.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("$", hasKey("id")); // Есть ли "id"

        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    @DisplayName("Запрос с повторяющимся логином")
    public void testDuplicateCourierCreation() {
        courierClient.create(courier);
        Response loginResponse = courierClient.login(courier);
        courierId = loginResponse.then().extract().path("id");

        Response response = courierClient.create(courier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .body("$", hasKey("message")); // Проверка структуры месседжа с ошибкой
    }

    @Test
    @DisplayName("Запрос без логина или пароля")
    public void testCourierCreationWithoutRequiredField() {
        Courier courierWithoutLogin = new Courier("", "password", "name");
        Response response = courierClient.create(courierWithoutLogin);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .body("$", hasKey("message")); // Проверка структуры мксседжа с ошибкой
    }
}