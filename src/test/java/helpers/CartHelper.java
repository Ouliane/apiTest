package helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import models.PostProductToCart;

public class CartHelper {
    public static Response getCart(String token) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .get("/cart");
    }

    public static Response postCart(String token, PostProductToCart product) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .body(product)
                .post("/cart");
    }

    public static Response deleteProductFromCart(String token, String productId) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .post("/cart/" + productId);
    }
}
