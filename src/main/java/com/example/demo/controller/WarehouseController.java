package com.example.demo.controller;

import com.example.demo.form.ProductWarehouseForm;
import com.example.demo.model.Product;
import com.example.demo.model.ProductWarehouse;
import com.example.demo.model.Warehouse;
import com.example.demo.service.product.IProductService;
import com.example.demo.service.productWarehouse.IProductWarehouseService;
import com.example.demo.service.warehouse.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private IProductWarehouseService productWarehouseService;

    @GetMapping("")
    public ModelAndView getAllWarehouse() {
        ModelAndView modelAndView = new ModelAndView("warehouse/list");

        modelAndView.addObject("warehouses", warehouseService.findAll());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createWarehouse(@ModelAttribute Warehouse warehouse) {
        ModelAndView modelAndView = new ModelAndView("warehouse/create");
        warehouseService.save(warehouse);

        modelAndView.addObject("warehouses", warehouse);
        modelAndView.addObject("message", "Tao moi thanh cong");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    private ModelAndView showEditWarehouse(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("warehouse/edit");
        Warehouse warehouse = warehouseService.findById(id);
        List<Product> product = productService.findAll();

        modelAndView.addObject("warehouses", warehouse);
        modelAndView.addObject("products", product);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    private ModelAndView editWarehouse(@ModelAttribute ProductWarehouseForm productWarehouseForm) {
        ModelAndView modelAndView = new ModelAndView("warehouse/edit");

        for (Product product: productWarehouseForm.getProducts()) {
            ProductWarehouse productWarehouse = new ProductWarehouse();
            productWarehouse.getId().setWarehouse(productWarehouseForm.getWarehouse());
            productWarehouse.getId().setProduct(product);

            productWarehouseService.save(productWarehouse);
        }

        warehouseService.save(productWarehouseForm.getWarehouse());

        modelAndView.addObject("warehouses", productWarehouseForm.getWarehouse());
        modelAndView.addObject("message", "Sua thanh cong");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteWarehouse(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/warehouse/manager");
        warehouseService.remove(id);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView getViewWarehouseById(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("warehouse/view");

        return modelAndView;
    }

}
