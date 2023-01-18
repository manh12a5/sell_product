package com.example.demo.service.product;

import com.example.demo.form.ProductForm;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.picture.IPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IPictureService pictureService;

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
        product.setDate(new Date(System.currentTimeMillis()));
        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product createProduct(ProductForm productForm) {
        Product product = null;
        if (productForm.getProductId() != null) {
            product = this.findById(productForm.getProductId());
        }

        if (product == null) {
            product = new Product();
        }

        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setDate(new Date(System.currentTimeMillis()));
        product.setCategory(productForm.getCategory());

        List<MultipartFile> multipartFileList = new ArrayList<>();
        if (productForm.getMainPicture() != null && !productForm.getMainPicture().isEmpty())
            multipartFileList.add(productForm.getMainPicture());
        if (productForm.getPicture1() != null && !productForm.getPicture1().isEmpty())
            multipartFileList.add(productForm.getPicture1());
        if (productForm.getPicture2() != null && !productForm.getPicture2().isEmpty())
            multipartFileList.add(productForm.getPicture2());

        Long pictureId = product.getPicture() != null ? product.getPicture().getId() : null;
        product.setPicture(pictureService.createPicture(multipartFileList, pictureId));

        return product;
    }

    @Override
    public ProductForm createProductForm(Product product) {
        ProductForm productForm = new ProductForm();
        productForm.setProductId(product.getId());
        productForm.setName(product.getName());
        productForm.setPrice(product.getPrice());
        productForm.setDescription(product.getDescription());
        productForm.setCategory(product.getCategory());
        productForm.setMainImage(product.getPicture().getMainPicture());
        productForm.setImage1(product.getPicture().getPicture1());
        productForm.setImage2(product.getPicture().getPicture2());

        return productForm;
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
    public Page<Product> findAllByOrderByNameAsc(Pageable pageable) {
        return productRepository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    public Page<Product> findAllByOrderByNameDesc(Pageable pageable) {
        return productRepository.findAllByOrderByNameDesc(pageable);
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
