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

public class RegisterCourierTest extends BaseTest {

    private Courier courier;
    private Integer courierId;

    @Test
    @DisplayName("Успешное создание курьера")
    void shouldCreateCourier() {
        courier = CourierGenerator.generateCourier("1234", "Test");
        Response response = CourierApi.createCourier(courier);

        ResponseSteps.checkStatusCode(response, 201);
        ResponseSteps.checkBody(response, "ok", true);

        Response loginResponse = CourierApi.loginCourier(
                new Login(courier.getLogin(), courier.getPassword())
        );
        if (loginResponse.statusCode() == 200) {
            courierId = loginResponse.jsonPath().getInt("id");
        }
    }

    @Test
    @DisplayName("Ошибка при создании курьера с существующим логином")
    void shouldNotCreateDuplicateCourier() {
        courier = CourierGenerator.generateCourier("1234", "Test");
        CourierApi.createCourier(courier);
        Response response = CourierApi.createCourier(courier);

        ResponseSteps.checkStatusCode(response, 409);
        ResponseSteps.checkBody(response, "message", "Этот логин уже используется. Попробуйте другой.");

        Response loginResponse = CourierApi.loginCourier(new Login(courier.getLogin(), courier.getPassword()));
        if (loginResponse.statusCode() == 200) {
            courierId = loginResponse.jsonPath().getInt("id");
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCouriers")
    @DisplayName("Ошибка при создании курьера - нет одного из полей")
    void shouldReturnErrorIfRequiredFieldMissing(Courier courier) {
        Response response = CourierApi.createCourier(courier);

        ResponseSteps.checkStatusCode(response, 400);
        ResponseSteps.checkBody(response, "message", "Недостаточно данных для создания учетной записи");
    }

    static Stream<Arguments> invalidCouriers() {
        return Stream.of(
                Arguments.of(
                        Named.of("[без логина]", new Courier(null, "1234", "Test"))
                ),
                Arguments.of(
                        Named.of("[без пароля]", new Courier("login", null, "Test"))
                )
        );
    }

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            CourierApi.deleteCourier(courierId);
            courierId = null;
        }
    }

}
