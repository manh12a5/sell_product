package com.example.demo.service.comment;

import com.example.demo.model.Comment;
import com.example.demo.service.IService;

import java.util.List;

public interface ICommentService extends IService<Comment> {
    List<Comment> getAllCommentByProductIdDESC(Long productId);
}
