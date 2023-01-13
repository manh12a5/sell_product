package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on", nullable = true, insertable = false, updatable = false)
    private Timestamp createOn;

    @OneToOne
    private AppUser appUser;

    @OneToOne
    private Shipment shipment;

}
