import models.UserModel;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static data.UserData.getRandomUser;
import static steps.UserSteps.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest extends BaseApiTest {

    private String token;

    @After
    public void cleanUp() {
        if (token != null) {
            deleteUser(token);
        }
    }

    @Test
    @DisplayName("Создаем нового пользователя")
    @Description("Проверяем, что пользователь успешно создан")
    public void testCreateUser() {
        UserModel user = getRandomUser();
        Response response = regUser(user);
        token = response.path("accessToken");
        response.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Ожидаем ошибку при создании уже существующего пользователя")
    @Description("Проверяем, что появляется ошибка 403 при потворной регистрации такого же пользователя")
    public void testCreateRepeatUser() {
        UserModel user = getRandomUser();
        Response firstRegistration = regUser(user);
        token = firstRegistration.path("accessToken");
        Response secondRegistration = regUser(user);
        secondRegistration.then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Ожидаем ошибку при создании пользователя без обязательного поля")
    @Description("Проверяем, что регистрация не прошла если не передано обязательное поле")
    public void testCreateUserNoName() {
        UserModel user = getRandomUser();
        user.setName(null);
        Response response = regUser(user);
        response.then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}