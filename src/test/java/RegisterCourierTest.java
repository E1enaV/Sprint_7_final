import org.junit.jupiter.api.DisplayName;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class RegisterCourierTest extends BaseTest {
    private final String login = RandomStringUtils.randomAlphabetic(10);
    private final String password = RandomStringUtils.randomAlphabetic(10);
    private final String firstName = RandomStringUtils.randomAlphabetic(10);

    @AfterEach
    void tearDown() {
        Courier registerCourierLogin = new Courier(login, password);
        String response = given()
                .body(registerCourierLogin)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        String userId = jsonPath.getString("id");
        delete(EndPoints.COURIER_REGISTER_OR_DELETE + userId);
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier when data is valid")
    void checkStatusCodeBodyCreateCourierWithValidData() {
        Courier courier = new Courier(login, password, firstName);
        given()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier when password field is missing")
    void checkStatusCodeBodyCreateCourierWithoutFillInPassword() {
        String registerBody = "{\"login\":\"" + login + "\"," +
                "\"firstName\":\"" + firstName + "\"}";
        given()
                .body(registerBody)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier when login field is missing")
    void checkStatusCodeBodyCreateCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\"," +
                "\"firstName\":\"" + firstName + "\"}";
        given()
                .body(registerBody)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier when login already exists")
    void checkStatusCodeBodyCreateCourierWithExistingLogin() {
        Courier firstCourier = new Courier(login, password, firstName);
        given()
                .body(firstCourier)
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .statusCode(HttpURLConnection.HTTP_CREATED);

        Courier secondCourier = new Courier(login, RandomStringUtils.randomAlphabetic(10), firstName);
        given()
                .body(secondCourier)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}

