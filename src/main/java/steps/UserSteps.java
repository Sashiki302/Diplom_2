package steps;
import models.UserModel;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static data.UserData.*;
import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Регистрация пользователя")
    public static Response regUser(UserModel user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Логин пользователя")
    public static Response loginUser(UserModel user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_DELETE);
    }
}