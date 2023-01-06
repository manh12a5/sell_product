package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Product product;

    @Column(name = "created_on", nullable = true, insertable = false, updatable = false)
    private Timestamp createOn;

    @Column(name = "updated_on", nullable = true, insertable = false, updatable = false)
    private Timestamp updateOn;

}
