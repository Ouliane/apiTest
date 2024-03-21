package helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Token;
import models.User;

public class AuthorizationHelper {
    public static void register(User user) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/register");
    }

    public static String authorise(User user) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/login")
                .then().extract().as(Token.class).getAccessToken();
    }
}
