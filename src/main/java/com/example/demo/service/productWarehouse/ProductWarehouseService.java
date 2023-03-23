package com.example.demo.service.productWarehouse;

import com.example.demo.model.ProductWarehouse;
import com.example.demo.repository.ProductWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductWarehouseService implements IProductWarehouseService {

    @Autowired
    private ProductWarehouseRepository productWarehouseRepository;

    @Override
    public List<ProductWarehouse> findAll() {
        return null;
    }

    @Override
    public ProductWarehouse findById(Long id) {
        return null;
    }

    @Override
    public ProductWarehouse save(ProductWarehouse productWarehouse) {
        return productWarehouseRepository.save(productWarehouse);
    }

    @Override
    public void remove(Long id) {

    }
}
