package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long> {

    @Query(value = "SELECT * FROM comment WHERE comment.product_id = :productId ORDER BY id DESC", nativeQuery = true)
    List<Comment> findAllCommentByProductIdDESC(@Param("productId") Long productId);
}

