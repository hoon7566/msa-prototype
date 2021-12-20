package com.example.catalogservice.domain.product;

import com.example.catalogservice.domain.catalog.Catalog;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
public class Products {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private String productName;

    private Integer stock;

    private Integer unitPrice;

    @ManyToOne @JoinColumn(name = "catalogId")
    private Catalog catalog;

    public void minusStock(){
        this.stock--;
    }
}
