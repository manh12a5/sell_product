package com.example.demo.service.product;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findProductByName(String name, Pageable pageable) {
        return productRepository.findProductByName(name, pageable);
    }

    @Override
    public List<Product> findListProductByName(String name) {
        return productRepository.findListProductByName(name);
    }

    @Override
    public Page<Product> findProductByCategoryName(Long id, Pageable pageable) {
        return productRepository.findProductByCategoryName(id, pageable);
    }

    @Override
    public Page<Product> findTop5ByOrderByPriceDesc(Pageable pageable) {
        return productRepository.findTop5ByOrderByPriceDesc(pageable);
    }

    @Override
    public Page<Product> findTop5ByOrderByPriceAsc(Pageable pageable) {
        return productRepository.findTop5ByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Product> findAllByOrderByPriceAsc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Product> findAllByOrderByPriceDesc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceDesc(pageable);
    }

    @Override
    public Product getCurrentProduct() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String product = authentication.getName();
        return productRepository.getProductByName(product);
    }

    @Override
    public Page<Product> getListProductByFilterPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.getListProductByFilterPrice(minPrice, maxPrice, pageable);
    }

}
