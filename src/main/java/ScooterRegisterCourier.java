import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class ScooterRegisterCourier {

    public ArrayList<String> registerNewCourierAndReturnLoginPassword() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        ArrayList<String> loginPass = new ArrayList<>();

        String registerRequestBody = String.format(
                "{\"login\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                courierLogin, courierPassword, courierFirstName
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(registerRequestBody)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

        if (response.statusCode() == 201) {
            loginPass.add(courierLogin);
            loginPass.add(courierPassword);
        }

        return loginPass;
    }
}

