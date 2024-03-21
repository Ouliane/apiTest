package models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart{

	@JsonProperty("total_discount")
	private Object totalDiscount;

	@JsonProperty("total_price")
	private Object totalPrice;

	@JsonProperty("cart")
	private List<ProductInCart> productList;
}