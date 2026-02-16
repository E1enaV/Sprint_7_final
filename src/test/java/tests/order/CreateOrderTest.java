package tests.order;

import io.restassured.response.Response;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import api.OrderApi;
import base.BaseTest;
import data.Order;
import utils.ResponseSteps;
import java.util.List;
import java.util.stream.Stream;

public class CreateOrderTest extends BaseTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("colors")
    void shouldCreateOrderWithDifferentColors(List<String> colors) {
        Order order = new Order(
                "Test",
                "Testovyi",
                "Gagarina 17",
                "2",
                "+79160000007",
                5,
                "2026-02-30",
                "Test comment",
                colors
        );

        Response response = OrderApi.createOrder(order);

        ResponseSteps.checkStatusCode(response, 201);
        ResponseSteps.checkFieldExists(response,"track");

    }

    static Stream<Arguments> colors() {
        return Stream.of (
                Arguments.of(
                        Named.of("[BLACK]", List.of("BLACK"))
                ),
                Arguments.of(
                        Named.of("[GREY]", List.of("GREY"))
                ),
                Arguments.of(
                        Named.of("[BLACK, GREY]", List.of("BLACK", "GREY"))
                ) ,
                Arguments.of(
                        Named.of("[NULL]", null)
                )
        );
    }
}
