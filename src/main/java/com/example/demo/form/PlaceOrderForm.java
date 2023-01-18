package com.example.demo.form;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderForm {

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String typePayment;
    private String shippingMethod;
    private String paymentMethod;

    private Long cartId;

    private Double subTotal;
    private Double discount;
    private Double shippingCost;
    private Double tax;
    private Double grandTotal;
}
