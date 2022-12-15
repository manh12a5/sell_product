package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_name")
    private String name;

    private Integer stock;

    @OneToMany(mappedBy = "id.warehouse", fetch = FetchType.LAZY)
    private Collection<ProductWarehouse> productWarehouseCollection;

}
