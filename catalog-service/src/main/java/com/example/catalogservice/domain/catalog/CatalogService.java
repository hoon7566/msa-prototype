package com.example.catalogservice.domain.catalog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public CatalogDto createCatalog(CatalogDto catalogDto){

        Catalog catalogEntity = modelMapper.map(catalogDto, Catalog.class);

        Catalog createCatalogEntity = catalogRepository.save(catalogEntity);

        return modelMapper.map(createCatalogEntity,CatalogDto.class);
    }

    public Iterable<Catalog> retrieveCatalog(){
        return catalogRepository.findAll();
    }

}
