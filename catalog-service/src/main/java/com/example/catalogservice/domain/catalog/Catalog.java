package com.example.catalogservice.domain.catalog;


import com.example.catalogservice.domain.product.Products;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "catalog")
public class Catalog {

    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long catalogId;

    private String catalogName;

    @OneToMany(mappedBy = "catalog")
    private List<Products> products;



}

