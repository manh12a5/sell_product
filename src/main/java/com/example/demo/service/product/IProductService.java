package com.example.demo.service.product;

import com.example.demo.form.ProductForm;
import com.example.demo.model.Product;
import com.example.demo.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IProductService extends IService<Product> {

    Page<Product> findAll(Pageable pageable);

    Product createProduct(@ModelAttribute("productForm") ProductForm productForm);

    ProductForm createProductForm(Product product);

    Page<Product> findProductByName(String name, Pageable pageable);

    List<Product> findListProductByName(String name);

    Page<Product> findProductByCategoryName(Long id, Pageable pageable);

    Page<Product> findTop5ByOrderByPriceDesc(Pageable pageable);

    Page<Product> findTop5ByOrderByPriceAsc(Pageable pageable);

    //Sắp xếp
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);

    //Sắp xếp
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Product> findAllByOrderByNameAsc(Pageable pageable);

    Page<Product> findAllByOrderByNameDesc(Pageable pageable);

    Product getCurrentProduct();

    Page<Product> getListProductByFilterPrice(Double minPrice, Double maxPrice, Pageable pageable);

}

