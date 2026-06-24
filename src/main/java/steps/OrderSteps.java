package steps;
import models.OrderModel;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static data.UserData.*;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Получение списка ингредиентов")
    public static Response getIngredients() {
        return given()
                .when()
                .get(INGREDIENTS_PATH);
    }

    @Step("Создание заказа с авторизацией")
    public static Response createOrderTrue(OrderModel order, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderFalse(OrderModel order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }
}
