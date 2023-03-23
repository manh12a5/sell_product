package com.example.demo.form;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartForm {
    private Long cartId;
    private Long productId;
    private int quantity;
    private int size;

    private List<CartItemForm> cartItemFormList = new ArrayList<>();
    private double totalPrice;
}
