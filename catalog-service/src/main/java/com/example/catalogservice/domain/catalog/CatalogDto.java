package com.example.catalogservice.domain.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatalogDto {

    private String productName;
    private Integer stock;
    private Integer unitPrice;


}
