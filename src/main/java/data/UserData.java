package data;
import com.github.javafaker.Faker;
import models.UserModel;

public class UserData {
    public static final String BASE_URI = "https://stellarburgers.education-services.ru";
    public static final String REGISTER_PATH = "/api/auth/register";
    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String USER_DELETE = "/api/auth/user";
    public static final String ORDERS_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "/api/ingredients";


    public static UserModel getRandomUser() {
        Faker faker = new Faker();
        return new UserModel(
                faker.internet().emailAddress(),
                faker.internet().password(4, 8),
                faker.name().username()
        );
    }
}
