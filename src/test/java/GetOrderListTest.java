import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderListTest extends BaseTest {

    @Test
    @DisplayName("Check status code and body of GET /api/v1/orders")
    void checkStatusCodeGetOrderList() {
        given()
                .when()
                .get(EndPoints.ORDER_CREATE_OR_GET)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("orders", notNullValue());
    }
}

