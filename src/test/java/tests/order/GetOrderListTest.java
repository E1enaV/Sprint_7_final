package tests.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import api.OrderApi;
import base.BaseTest;
import utils.ResponseSteps;

public class GetOrderListTest extends BaseTest {

    @Test
    @Step("Получение списка заказов")
    void shouldReturnOrdersList() {
        Response response = OrderApi.getOrders();

        ResponseSteps.checkStatusCode(response, 200);
        ResponseSteps.checkFieldExists(response,"orders");
    }
}