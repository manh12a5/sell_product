package com.example.demo.form;

import com.example.demo.model.Product;
import com.example.demo.model.Warehouse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductWarehouseForm {

    private Warehouse warehouse;
    private List<Product> products;

}
