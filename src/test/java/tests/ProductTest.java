package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.ProductHelper;
import io.restassured.response.Response;
import models.NewProduct;
import models.ProductInfo;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductTest extends AbstractApiTest {
    @Test
    @DisplayName("Позитивный кейс GET /products")
    public void getListOfProductsTest() {
        Response productListResponse = ProductHelper.getProductList();
        List<ProductInfo> productInfo = productListResponse.as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ProductInfo.class));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(productListResponse.getStatusCode())
                .isEqualTo(200);
        softly.assertThat(productInfo)
                .isNotEmpty()
                .isNotNull();
        softly.assertAll();
    }

    @Test
    @DisplayName("Позитивный кейс POST /products")
    public void addNewProductTest() {
        NewProduct product = new NewProduct("New Product", "Electronics", "12.99", "5");
        Response addNewProductResponse = ProductHelper.addNewProduct(product);

        assertThat(addNewProductResponse.getStatusCode())
                .isEqualTo(201);
    }

    @Test
    @DisplayName("Позитивный кейс GET /products/{product_id}")
    public void getProductTest() {
        Response productInfoResponse = ProductHelper.getProduct("1");
        List<ProductInfo> productInfo = productInfoResponse.as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ProductInfo.class));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(productInfo)
                .isNotEmpty()
                .isNotNull();
        softly.assertThat(productInfoResponse.getStatusCode())
                .isEqualTo(200);
        softly.assertAll();
    }

    @Test
    @DisplayName("Негативный кейс GET /products/{product_id} (продукта с таким айди нет)")
    public void negativeGetProductTest() throws JsonProcessingException {
        Response productInfoResponse = ProductHelper.getProduct("999");
        String response = productInfoResponse.getBody().print();
        String message = new ObjectMapper().readTree(response).get("message").asText();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(message)
                .isEqualTo("Product not found");
        softly.assertThat(productInfoResponse.getStatusCode())
                .isEqualTo(404);
        softly.assertAll();
    }

    @Test
    @DisplayName("Позитивный кейс PUT /products")
    public void putProductTest() {
        String productId = "1";
        Response productInfoResponse = ProductHelper.getProduct(productId);
        List<ProductInfo> productInfo = productInfoResponse.as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ProductInfo.class));

        String newDiscount = "77";
        NewProduct updatedProduct = new NewProduct(productInfo.get(0).getName(), productInfo.get(0).getCategory(), productInfo.get(0).getPrice(), newDiscount);

        Response addNewProductResponse = ProductHelper.putProduct(updatedProduct, productId);

        assertThat(addNewProductResponse.getStatusCode())
                .isEqualTo(200);

        Response newProductInfoResponse = ProductHelper.getProduct(productId);
        List<ProductInfo> newProductInfo = newProductInfoResponse.as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ProductInfo.class));
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(newProductInfo.get(0).getName())
                .isEqualTo(productInfo.get(0).getName());
        softly.assertThat(newProductInfo.get(0).getCategory())
                .isEqualTo(productInfo.get(0).getCategory());
        softly.assertThat(newProductInfo.get(0).getPrice())
                .isEqualTo(productInfo.get(0).getPrice());
        softly.assertThat(newProductInfo.get(0).getDiscount())
                .isEqualTo(newDiscount);
        softly.assertAll();
    }

    @Test
    @DisplayName("Негативный кейс PUT /products (Несуществующий id)")
    public void negativePutProductTest() {
        String productId = "1";
        Response productInfoResponse = ProductHelper.getProduct(productId);
        List<ProductInfo> productInfo = productInfoResponse.as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ProductInfo.class));
        NewProduct updatedProduct = new NewProduct(productInfo.get(0).getName(), productInfo.get(0).getCategory(), productInfo.get(0).getPrice(), productInfo.get(0).getDiscount());

        Response addNewProductResponse = ProductHelper.putProduct(updatedProduct, "789");

        assertThat(addNewProductResponse.getStatusCode())
                .isEqualTo(404);
    }

    @Test
    @DisplayName("Позитивный кейс DELETE /products/{product_id}")
    public void deleteProductTest() {
        String productId = "1";
        Response productInfoResponse = ProductHelper.getProduct(productId);
        assertThat(productInfoResponse.getStatusCode())
                .isEqualTo(200);

        Response addNewProductResponse = ProductHelper.deleteProduct(productId);

        assertThat(addNewProductResponse.getStatusCode())
                .isEqualTo(200);

        productInfoResponse = ProductHelper.getProduct(productId);
        assertThat(productInfoResponse.getStatusCode())
                .isEqualTo(404);
    }
}
