package com.example.demo.model;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    @OneToOne
    private Cart cart;

    private int quantity;
    private int size;
    private Boolean status = false;

}
