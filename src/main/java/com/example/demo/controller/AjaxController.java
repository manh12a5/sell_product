package com.example.demo.controller;

import com.example.demo.form.CommentForm;
import com.example.demo.model.AppUser;
import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.comment.ICommentService;
import com.example.demo.service.login.IAppUserService;
import com.example.demo.service.product.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/ajax")
@Slf4j
public class AjaxController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    private AppUser getLoginUser() {
        return appUserService.getCurrentUser();
    }

    @GetMapping("/findProducts")
    public List<Product> findProducts(@RequestParam("nameProduct") String nameProduct) {
        log.info("[findProducts]: {}", nameProduct);
        return productService.findListProductByName(nameProduct);
    }

    @PostMapping(value = "/createComment",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Comment createNewComment(@RequestBody CommentForm commentForm) {
        if (getLoginUser() != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Comment comment = new Comment();

            comment.setComment(commentForm.getComment());
            comment.setUser(getLoginUser());
            comment.setProduct(productService.findById(commentForm.getProductId()));
            comment.setCreatedOn(timestamp);
            comment.setUpdatedOn(timestamp);
            commentService.save(comment);

            return comment;
        } else {
            log.debug("Error add comment");
        }
        return null;
    }

    @PostMapping(value = "/editComment",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Comment editComment(@RequestBody CommentForm commentForm) {
        if (getLoginUser() != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Comment comment = commentRepository.findById(commentForm.getCommentId()).orElse(null);
            if(comment != null) {
                comment.setComment(commentForm.getComment());
                comment.setUpdatedOn(timestamp);
                commentService.save(comment);
            }
            return comment;
        } else {
            log.debug("Error edit comment");
        }
        return null;
    }

    @GetMapping(value = "/deleteComment")
    public Boolean deleteComment(@RequestParam(value = "commentId") String commentId) {
        if (getLoginUser() != null) {
            Long id = Long.parseLong(commentId);
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null && comment.getUser().getId().equals(getLoginUser().getId())) {
                commentService.remove(id);
                return true;
            }
        }
        return false;
    }

}
