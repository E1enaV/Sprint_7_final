import org.junit.jupiter.api.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseTest {

    private String[] color;
    private int track;

    @ParameterizedTest
    @DisplayName("Check status code and track of /api/v1/orders when data is valid")
    @MethodSource("getOrderData")
    void checkStatusCodeAndBodyCreateOrderWithValidData(String[] colorParam) {
        this.color = colorParam;

        Order order = new Order(color);
        order.setUpFieldsForRequest();

        Response response = given()
                .body(order)
                .post(EndPoints.ORDER_CREATE_OR_GET);

        response.then()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue());

        track = new JsonPath(response.asString()).getInt("track");
    }

    @AfterEach
    void tearDown() {
        if (track > 0) {
            String cancelBody = "{\"track\":" + track + "}";
            given()
                    .body(cancelBody)
                    .put(EndPoints.ORDER_CANCEL);
        }
    }

    static Object[][] getOrderData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }
}







