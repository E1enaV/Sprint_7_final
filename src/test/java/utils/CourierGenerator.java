package utils;

import data.Courier;
import io.qameta.allure.Step;
import java.util.UUID;

public class CourierGenerator {

    @Step("Генерация данных курьера")
    public static Courier generateCourier(String password, String firstName) {
        String login = "any_login" + UUID.randomUUID();
        return new Courier(login, password, firstName);
    }

}
