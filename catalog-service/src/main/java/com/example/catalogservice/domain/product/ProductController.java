package com.example.catalogservice.domain.product;

import com.example.catalogservice.domain.catalog.Catalog;
import com.example.catalogservice.domain.catalog.CatalogDto;
import com.example.catalogservice.domain.catalog.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/productStock/{productId}")
    public ResponseEntity<Integer> productStock(@PathVariable Long productId){
        log.info("================> productStock");
        Integer productStock = productService.productStock(productId);
        log.info("================> productStock = "+ productStock);
        return ResponseEntity.status(HttpStatus.OK).body(productStock);

    }
}
