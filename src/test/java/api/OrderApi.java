package api;

import config.Config;
import data.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    @Step("Создание заказа")
    public static Response createOrder(Order order) {
        return given()
                .contentType("application/json")
                .body(order)
                .post(Config.ORDER_CREATE_OR_GET);
    }

    @Step("Получение списка заказов")
    public static Response getOrders() {
        return given()
                .get(Config.ORDER_CREATE_OR_GET);
    }
}
