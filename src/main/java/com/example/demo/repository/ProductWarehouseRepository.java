package com.example.demo.repository;

import com.example.demo.model.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {
}
