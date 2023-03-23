package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    //Tìm kiếm sản phẩm theo tên
    @Query(value = "select * from category where category.name like ?", nativeQuery = true)
    List<Product> findCategoriesByName(String name);

}
