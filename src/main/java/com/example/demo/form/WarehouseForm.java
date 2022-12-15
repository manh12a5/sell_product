package com.example.demo.form;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseForm {

    private String name;
    private Integer stock;
}
