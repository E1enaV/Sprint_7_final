package tests.courier;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import api.CourierApi;
import base.BaseTest;
import data.Courier;
import data.Login;
import utils.CourierGenerator;
import utils.ResponseSteps;

import java.util.stream.Stream;

public class LoginCourierTest extends BaseTest {

    private Courier courier;
    private Integer courierId;

    @Test
    @DisplayName("Успешная авторизация курьера")
    void shouldLoginCourier() {
        courier = CourierGenerator.generateCourier("1234", "Test");
        CourierApi.createCourier(courier);
        Login login = new Login(courier.getLogin(), courier.getPassword());
        Response response = CourierApi.loginCourier(login);

        ResponseSteps.checkStatusCode(response, 200);
        ResponseSteps.checkFieldExists(response, "id");

        courierId = response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Ошибка при неверном пароле")
    void shouldReturnErrorForWrongPassword() {
        courier = CourierGenerator.generateCourier("1234", "Test");
        CourierApi.createCourier(courier);
        Login login = new Login(courier.getLogin(), "wrong");
        Response response = CourierApi.loginCourier(login);

        ResponseSteps.checkStatusCode(response, 404);
        ResponseSteps.checkBody(response, "message", "Учетная запись не найдена");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidLogins")
    @DisplayName("Ошибка при отсутствии обязательного поля")
    void shouldReturnErrorIfRequiredFieldMissing(Login login) {
        Response response = CourierApi.loginCourier(login);

        ResponseSteps.checkStatusCode(response, 400);
        ResponseSteps.checkBody(response, "message", "Недостаточно данных для входа");
    }

    static Stream<Arguments> invalidLogins() {
        return Stream.of(
                Arguments.of(
                        Named.of("[без логина]", new Login(null, "1234"))
                ),
                Arguments.of(
                        Named.of("[без пароля]", new Login("login", null))
                )
        );
    }

    @Test
    @DisplayName("Ошибка при несуществующем курьере")
    void shouldReturnErrorForNonExistentCourier() {
        Login login = new Login("unknown_login", "1234");
        Response response = CourierApi.loginCourier(login);

        ResponseSteps.checkStatusCode(response, 404);
        ResponseSteps.checkBody(response, "message", "Учетная запись не найдена");
    }

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            CourierApi.deleteCourier(courierId);
        }
    }
}




