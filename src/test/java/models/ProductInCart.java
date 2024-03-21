package models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCart {
    private String category;
    private String discount;
    private String id;
    private String name;
    private String price;
    private int quantity;
}
