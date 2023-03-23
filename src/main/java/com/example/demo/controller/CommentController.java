package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.AppUser;
import com.example.demo.model.Product;
import com.example.demo.service.comment.ICommentService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    IProductService productService;

    @Autowired
    ICommentService commentService;

    @Autowired
    IAppUserService appUserService;

    @ModelAttribute
    public AppUser currentUser() {
        return appUserService.getCurrentUser();
    }

    @ModelAttribute()
    public Product currentProduct(){
        return productService.getCurrentProduct();
    }

    @GetMapping("/{productId}")
    public ModelAndView getAllListCommentByProduct(@PathVariable Long productId) {
        ModelAndView modelAndView = new ModelAndView("comment/list");
        modelAndView.addObject("allComment", commentService.getAllCommentByProductIdDESC(productId));
        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView formCreate() {
        ModelAndView mav = new ModelAndView("comment/create");
        mav.addObject("comment", new Comment());
        return mav;
    }

    @PostMapping(value = "/createComment",
            produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment create(@RequestBody Comment comment){
        AppUser appUser=currentUser();
        Product product=currentProduct();
        comment.setUser(appUser);
        comment.setProduct(product);
        System.out.println("id"+product);
        return commentService.save(comment);
    }

    @GetMapping (value = "/edit/{id}")
    public ModelAndView formEdit(@PathVariable Long id){
        Comment comment = commentService.findById(id);
        if(comment != null) {
            ModelAndView modelAndView = new ModelAndView("comment/edit");
            modelAndView.addObject("comment",comment);
            return modelAndView;
        }
        return null;
    }
    @ResponseBody
    @PostMapping(value = "/edit/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Comment> edit(@PathVariable Long id, @RequestBody Comment comment){
        comment.setId(id);
        AppUser appUser=currentUser();
        Product product=currentProduct();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String a = commentService.findById(id).getUser().getFirstName();
        boolean b = appUser.getFirstName().equals(a);
        if (b){
            comment.setUser(appUser);
            comment.setProduct(product);
            System.out.println(product);
            comment.setUpdatedOn(timestamp);
            commentService.save(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(value = "/delete/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Comment> delete(@PathVariable Long id) {
            AppUser appUser=currentUser();
        String a = commentService.findById(id).getUser().getFirstName();
        boolean b = appUser.getFirstName().equals(a);
        if (b){
            commentService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }

}
