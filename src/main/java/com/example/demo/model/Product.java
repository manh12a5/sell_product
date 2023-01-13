package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @OneToOne
    private Picture picture;

    private Date date;

    private String description;

    @ManyToOne
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "id.product", fetch = FetchType.LAZY)
    private Collection<ProductWarehouse> productWarehouseCollection;

}
