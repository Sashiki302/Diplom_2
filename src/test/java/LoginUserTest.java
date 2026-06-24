import models.UserModel;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static data.UserData.getRandomUser;
import static steps.UserSteps.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseApiTest {

    private UserModel user;
    private String token;

    @Before
    public void registerBeforeLogin() {
        user = getRandomUser();
        Response response = regUser(user);
        token = response.path("accessToken");
    }

    @After
    public void cleanUp() {
        if (token != null) {
            deleteUser(token);
        }
    }

    @Test
    @DisplayName("Успешный вход под существующим пользователем")
    @Description("Проверяем, что вход прошел успешно")
    public void testTrueLogin() {
        UserModel credentials = new UserModel(user.getEmail(), user.getPassword());
        Response response = loginUser(credentials);
        response.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным логином и паролем")
    @Description("Проверяем, что появляется ошибка 401 при вводе некорректного логина и пароля")
    public void testLoginWrongLogPass() {
        UserModel badCredentials = new UserModel("helloworld@yandex.ru", "12345678");
        Response response = loginUser(badCredentials);
        response.then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}