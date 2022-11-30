package com.example.demo.form;

import com.example.demo.model.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemForm {
    private Long cartItemId;
    private int size;
    private int quantity;
    private double total;
    private Product product;
}
