import models.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static data.UserData.getRandomUser;
import static steps.OrderSteps.*;
import static steps.UserSteps.deleteUser;
import static steps.UserSteps.regUser;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest extends BaseApiTest {

    private String token;
    private List<String> validIngredients;

    @Before
    public void newUser() {
        UserModel user = getRandomUser();
        Response regResponse = regUser(user);
        token = regResponse.path("accessToken");
        Response ingResponse = getIngredients();
        validIngredients = ingResponse.path("data._id");
    }

    @After
    public void cleanUp() {
        if (token != null) {
            deleteUser(token);
        }
    }

    @Test
    @DisplayName("Создаем заказ с авторизацией и ингредиентами")
    @Description("Проверяем создание заказа авторизованным пользователем с существующими ингредиентами")
    public void testCreateOrderTrueAuth() {
        List<String> orderIngredients = List.of(validIngredients.get(0), validIngredients.get(1));
        OrderModel order = new OrderModel(orderIngredients);
        Response response = createOrderTrue(order, token);
        response.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создаем заказ без авторизации")
    @Description("Проверяем создание заказа неавторизованного пользователя")
    public void testCreateOrderNoAuth() {
        List<String> orderIngredients = List.of(validIngredients.get(0));
        OrderModel order = new OrderModel(orderIngredients);
        Response response = createOrderFalse(order);
        response.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создаем заказ без ингредиентов")
    @Description("Проверяем, что появляется ошибка 400 при отправке пустого ингредиента")
    public void testCreateOrderNoIngredients() {
        OrderModel order = new OrderModel(new ArrayList<>());
        Response response = createOrderTrue(order, token);
        response.then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создаем заказ с неверным хешем ингредиента")
    @Description("Проверяем, что появляется ошибка 400 при передаче несуществующего хеша ингредиента")
    public void testCreateOrderFalseHash() {
        OrderModel order = new OrderModel(List.of("61c0c5a71d1f82001bdaaa6d123123123"));
        Response response = createOrderTrue(order, token);
        response.then()
                .statusCode(HTTP_BAD_REQUEST);
    }
}