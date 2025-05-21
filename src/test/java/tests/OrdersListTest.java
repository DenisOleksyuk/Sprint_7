package tests;

import clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrdersListTest {
    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrdersList() {
        Response response = new OrderClient().getOrders();
        response.then()
                .statusCode(200)
                .body("orders", not(empty()))
                .body("$", hasKey("orders")) // Проверка наличия поля
                .body("orders[0]", hasKey("id")) // Проверка структуру первого заказа
                .body("orders[0]", hasKey("track"));
    }
}