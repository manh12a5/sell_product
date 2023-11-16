package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.s3.S3Bucket;
import com.example.demo.service.category.CategoryServiceImp;
import com.example.demo.service.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @Autowired
    Environment environment;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private S3Bucket s3Bucket;

    @Value("${aws.s3.url:https://sell-product.s3.ap-southeast-1.amazonaws.com}")
    private String URL_S3;

    @GetMapping("")
    public ModelAndView showCategories(){
        ModelAndView modelAndView = new ModelAndView("category/list");
        modelAndView.addObject("categories", categoryServiceImp.findAll());
        return modelAndView;
    }

    @GetMapping("/create-cate")
    public ModelAndView showFormCreateCate(){
        ModelAndView modelAndView = new ModelAndView("category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/create-cate")
    public ModelAndView createCate(@ModelAttribute Category category) throws IOException {
        ModelAndView mav = new ModelAndView("category/create");
        MultipartFile multipartFile = category.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        s3Service.putS3Object(s3Bucket.getCustomer(), fileName, multipartFile.getBytes());

        category.setImage(fileName);
        category.setName(category.getName());
        categoryServiceImp.save(category);

        mav.addObject("category", category);
        mav.addObject("message", "New customer created successfully");
        return mav;
    }

    @GetMapping("/edit-cate/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Category category = categoryServiceImp.findById(id);
        if(category != null) {
            ModelAndView modelAndView = new ModelAndView("category/edit");
            modelAndView.addObject("category", category);
            return modelAndView;

        }else {
            return new ModelAndView("/error.404");
        }
    }

    @PostMapping("/edit-cate")
    public ModelAndView updateCate(@ModelAttribute("category") Category category) throws IOException {
        ModelAndView modelAndView = new ModelAndView("category/edit");
        Category c = categoryServiceImp.findById(category.getId());
        if (category.getAvatar() != null) {
            MultipartFile multipartFile = category.getAvatar();
            String fileName = multipartFile.getOriginalFilename();
            s3Service.putS3Object(s3Bucket.getCustomer(), fileName, multipartFile.getBytes());

            c.setImage(fileName);
        }
        if (category.getName() != null && !category.getName().isEmpty()) {
            c.setName(category.getName());
        }

        categoryServiceImp.save(c);
        modelAndView.addObject("category", c);
        modelAndView.addObject("message", "Category updated successfully");
        return modelAndView;
    }

    @GetMapping("/del-cate/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Category category = categoryServiceImp.findById(id);
        if(category != null) {
            ModelAndView modelAndView = new ModelAndView("category/delete");
            modelAndView.addObject("category", category);
            return modelAndView;

        }else {
            return new ModelAndView("/error.404");
        }
    }

    @PostMapping("/del-cate")
    public ModelAndView deleteCate(@ModelAttribute("category") Category category){
        categoryServiceImp.remove(category.getId());
        return new ModelAndView("redirect:/categories");
    }

}
