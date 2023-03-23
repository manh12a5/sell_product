package com.example.demo.model;

import com.example.demo.model.pk.ProductWarehousePk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_warehouse")
public class ProductWarehouse {

    @EmbeddedId
    private ProductWarehousePk id;

    @Column(name = "is_main_warehouse", nullable = false)
    Boolean mainWarehouse;

}
