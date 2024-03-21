package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.AuthorizationHelper;
import helpers.CartHelper;
import io.restassured.response.Response;
import models.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CartTest extends AbstractApiTest {
    private static final String USERNAME = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    private static final String PASSWORD = "123";
    private static String token;

    @BeforeAll
    public static void beforeAll() {
        User user = new User(USERNAME, PASSWORD);
        AuthorizationHelper.register(user);
        token = AuthorizationHelper.authorise(user);
    }

    @Test
    @DisplayName("Позитивный кейс GET /cart")
    public void getProductCartTest() {
        PostProductToCart product = new PostProductToCart("1", 2);
        CartHelper.postCart(token, product);
        Response productListResponse = CartHelper.getCart(token);
        ShoppingCart shoppingCart = productListResponse.as(ShoppingCart.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(productListResponse.getStatusCode())
                .isEqualTo(200);
        softly.assertThat(shoppingCart)
                .isNotNull();
        softly.assertAll();
    }

    @Test
    @DisplayName("Позитивный кейс POST /cart")
    public void postProductCartTest() throws JsonProcessingException {
        PostProductToCart product = new PostProductToCart("1", 2);
        Response response = CartHelper.postCart(token, product);
        String stringResponse = response.getBody().print();
        String message = new ObjectMapper().readTree(stringResponse).get("message").asText();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(message)
                .isEqualTo("Product added to cart successfully");
        softly.assertThat(response.getStatusCode())
                .isEqualTo(201);
        softly.assertAll();
    }

    @Test
    @DisplayName("Негативный кейс POST /cart (product_id=null)")
    public void negativePostProductCartTest() throws JsonProcessingException {
        PostProductToCart product = new PostProductToCart();
        product.setQuantity(5);
        Response response = CartHelper.postCart(token, product);
        String stringResponse = response.getBody().print();
        String message = new ObjectMapper().readTree(stringResponse).get("message").asText();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(message)
                .isEqualTo("Product not found");
        softly.assertThat(response.getStatusCode())
                .isEqualTo(404);
        softly.assertAll();
    }

    @Test
    @DisplayName("Позитивный кейс DELETE /cart/{product_id}")
    public void deleteProductFromCartTest() {
        String productId = "1";
        PostProductToCart product = new PostProductToCart(productId, 2);
        CartHelper.postCart(token, product);
        Response response = CartHelper.deleteProductFromCart(token, productId);
        assertThat(response.getStatusCode())
                .isEqualTo(200);
    }

    @Test
    @DisplayName("Негатиный кейс DELETE /cart/{product_id} (невалидный токен)")
    public void negativeDeleteProductFromCartTest() {
        String productId = "1";
        PostProductToCart product = new PostProductToCart(productId, 2);
        CartHelper.postCart(token, product);
        Response response = CartHelper.deleteProductFromCart("token", productId);
        assertThat(response.getStatusCode())
                .isEqualTo(405);
    }
}
