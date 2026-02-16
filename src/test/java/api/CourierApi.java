package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import config.Config;
import data.Courier;
import data.Login;

import static io.restassured.RestAssured.given;

public class CourierApi {

    @Step("Создание курьера")
    public static Response createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(Config.COURIER_ENDPOINT);
    }

    @Step("Авторизация курьера")
    public static Response loginCourier(Login login) {
        return given()
                .contentType(ContentType.JSON)
                .body(login)
                .post(Config.COURIER_LOGIN);
    }

    @Step("Удаление курьера")
    public static void deleteCourier(int id) {
        given()
                .delete(Config.COURIER_ENDPOINT + "/" + id);
    }
}


