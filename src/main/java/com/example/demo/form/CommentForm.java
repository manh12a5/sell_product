package com.example.demo.form;

import lombok.Data;

@Data
public class CommentForm {

    private Long commentId;
    private String comment;
    private Long productId;
}
