package clients;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private static final Gson gson = new Gson();

    @Step("Создать курьера")
    public Response create(Courier courier) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(gson.toJson(courier)) // Сериализация в JSON
                .when()
                .post("/api/v1/courier");
    }

    @Step("Авторизовать курьера")
    public Response login(Courier courier) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(gson.toJson(courier)) // Сериализация в JSON
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера")
    public void delete(int courierId) {
        given()
                .baseUri(BASE_URL)
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}