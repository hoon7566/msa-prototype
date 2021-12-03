package com.example.userservice.user.jpa;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "catalog")
public class CatalogEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long id;

    @Column(nullable = false , length = 50, unique = true)
    private String productName;

    @Column(nullable = false , length = 50)
    private Integer stock;

    @Column(nullable = false , length = 50)
    private Integer unitPrice;


}

