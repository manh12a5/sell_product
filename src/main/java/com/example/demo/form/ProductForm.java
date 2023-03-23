package com.example.demo.form;

import com.example.demo.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {

    private Long productId;

    private String name;
    private Double price;
    private String description;
    private Category category;

    private MultipartFile mainPicture;
    private String mainImage;

    private MultipartFile picture1;
    private String image1;

    private MultipartFile picture2;
    private String image2;
}
