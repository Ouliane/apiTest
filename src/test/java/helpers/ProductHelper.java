package helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.NewProduct;

public class ProductHelper {
    public static Response getProductList() {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .get("/products");
    }

    public static Response addNewProduct(NewProduct product) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(product)
                .post("/products");
    }

    public static Response getProduct(String productId) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .get("/products/" + productId);
    }

    public static Response putProduct(NewProduct product, String productId) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(product)
                .put("/products/" + productId);
    }

    public static Response deleteProduct(String productId) {
        return RestAssured.given()
                .delete("/products/" + productId);
    }
}
