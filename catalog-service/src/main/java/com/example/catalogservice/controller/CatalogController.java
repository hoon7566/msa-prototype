package com.example.catalogservice.controller;

import com.example.catalogservice.dto.CatalogDto;
import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
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
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;
    private final ModelMapper modelMapper;

    @GetMapping("/welcome")
    public String welcome(){
        return String.format("It's Working in Catalog Service"
                +" Port (local.server.port) = " +env.getProperty("local.server.port")
                +" Port (server.port) = " +env.getProperty("server.port")
        );

    }

    @PostMapping("/orders")
    public ResponseEntity<CatalogDto> createCatalog(@RequestBody CatalogDto catalogDto){

        CatalogDto createCatalog = catalogService.createCatalog(catalogDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(createCatalog);

    }

    @GetMapping("/orders")
    public ResponseEntity<List<CatalogDto>> retrieveCatalogs(){

        Iterable<CatalogEntity> catalogs= catalogService.retrieveCatalog();

        List<CatalogDto> catalogList = new ArrayList<>();

        catalogs.forEach(e -> catalogList.add(modelMapper.map(e,CatalogDto.class)));

        return ResponseEntity.status(HttpStatus.OK).body(catalogList);

    }
}
