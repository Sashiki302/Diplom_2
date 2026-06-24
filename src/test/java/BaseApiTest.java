import io.restassured.RestAssured;
import org.junit.Before;
import static data.UserData.BASE_URI;

public class BaseApiTest {
    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }
}