package com.example.demo.controller;

import com.example.demo.form.CartForm;
import com.example.demo.form.ProductForm;
import com.example.demo.form.SortFilterForm;
import com.example.demo.form.WarehouseForm;
import com.example.demo.model.AppUser;
import com.example.demo.model.Category;
import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.category.ICategoryService;
import com.example.demo.service.comment.ICommentService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import com.example.demo.service.warehouse.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private ICommentService commentService;

    @ModelAttribute("listCategory")
    private List<Category> listCate() {
        return categoryService.findAll();
    }

    private AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    //Show All
    @GetMapping("")
    private ModelAndView showAll(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("view/shop");
        List<Category> categories = categoryService.findAll();
        Page<Product> productPage = productService.findAll(pageable);
        Long numberOfProducts = productPage.getTotalElements();

        modelAndView.addObject("products", productPage);
        modelAndView.addObject("categoriesProduct", categories);
        modelAndView.addObject("numberOfProducts", numberOfProducts);
        return modelAndView;
    }

    @GetMapping("/manager")
    private ModelAndView showAllFormAdmin(@PageableDefault(size = 6) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        modelAndView.addObject("products", productService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/create")
    private ModelAndView showCreate() {
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("product", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/create")
    private ModelAndView create(@ModelAttribute("product") ProductForm productForm) {
        ModelAndView modelAndView = new ModelAndView("product/create");
        Product product = productService.createProduct(productForm);
        productService.save(product);

        modelAndView.addObject("product", new ProductForm());
        modelAndView.addObject("message", "Tao moi thanh cong");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    private ModelAndView showEdit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("product/edit");
        Product product = productService.findById(id);
        ProductForm productForm = productService.createProductForm(product);

        modelAndView.addObject("product", productForm);
        return modelAndView;
    }

    @PostMapping("/edit")
    private ModelAndView edit(@ModelAttribute("product") ProductForm productForm) {
        ModelAndView modelAndView = new ModelAndView("product/edit");
        Product product = productService.createProduct(productForm);
        productService.save(product);

        ProductForm productF = productService.createProductForm(product);

        modelAndView.addObject("product", productF);
        modelAndView.addObject("message", "Sua thanh cong");
        return modelAndView;
    }

    @GetMapping ("/delete/{id}")
    public ModelAndView showDelete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/products/manager");
        productService.remove(id);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewDetail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("view/shop-detail");
        Product product = productService.findById(id);
        List<Comment> comments = commentService.getAllCommentByProductIdDESC(product.getId());
        List<WarehouseForm> warehouseForms = warehouseService.findWarehouseFormByProductId(product.getId());

        modelAndView.addObject("product", product);
        modelAndView.addObject("productId", id);
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("cartForm", new CartForm());
        modelAndView.addObject("warehouse", warehouseForms);
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView showSearchProduct(@RequestParam String name, @PageableDefault(size = 6) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("fragment/header");
        String nameProduct = "%" + name + "%";
        Page<Product> productList = productService.findProductByName(nameProduct, pageable);
        modelAndView.addObject("products", productList);
        return modelAndView;
    }

    @PostMapping("/searchName")
    public ModelAndView showSearchNameProduct(@RequestParam String name, @PageableDefault(size = 6) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("view/shop");
        String nameProduct = name + "%";
        Page<Product> productList = productService.findProductByName(nameProduct, pageable);
        modelAndView.addObject("products", productList);
        return modelAndView;
    }

    @PostMapping("/searchCategory")
    public ModelAndView searchProductByCategory(@RequestParam Long id, @PageableDefault(size = 6) Pageable pageable) {
        Page<Product> productPage = productService.findProductByCategoryName(id, pageable);
        return new ModelAndView("view/shop", "products", productPage);
    }

    @GetMapping("/sortProduct")
    public ModelAndView sortPriceMax(@RequestParam String typeSort, @PageableDefault(size = 6) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("view/shop");
        Page<Product> products = null;
        switch (typeSort) {
            case "1":
                products = productService.findAllByOrderByNameAsc(pageable);
                break;
            case "2":
                products = productService.findAllByOrderByNameDesc(pageable);
                break;
            case "3":
                products = productService.findAllByOrderByPriceDesc(pageable);
                break;
            case "4":
                products = productService.findAllByOrderByPriceAsc(pageable);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeSort);
        }

        modelAndView.addObject("products", products);
        modelAndView.addObject("typeSort", typeSort);
        modelAndView.addObject("numberOfProducts", products.getTotalElements());
        return modelAndView;
    }

    @GetMapping("/sortFilter")
    public ModelAndView sortFilter(@ModelAttribute("sortFilterForm") SortFilterForm sortFilterForm,
                                   @PageableDefault(size = 6) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("view/shop");
        Page<Product> products = productService
                .getListProductByFilterPrice(sortFilterForm.getMinPrice(), sortFilterForm.getMaxPrice(), pageable);
        List<Category> categories = categoryService.findAll();

        modelAndView.addObject("products", products);
        modelAndView.addObject("categoriesProduct", categories);
        modelAndView.addObject("numberOfProducts", products.getTotalElements());

        return modelAndView;
    }

}
