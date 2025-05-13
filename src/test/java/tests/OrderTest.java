package tests;

import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderTest {
    private final List<String> colors;

    public OrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void testCreateOrderWithColors() {
        Order order = new Order(colors);
        Response response = new OrderClient().create(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue())
                .body("$", hasKey("track")) // Проверка наличие поля
                .body("track", is(greaterThan(0))); // Проверка что track число > 0
    }
}