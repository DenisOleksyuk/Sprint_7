package clients;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private static final Gson gson = new Gson();

    @Step("Создать заказ")
    public Response create(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(gson.toJson(order)) // Сериализация в JSON
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получить список заказов")
    public Response getOrders() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/v1/orders");
    }
}