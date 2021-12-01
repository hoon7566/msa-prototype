package com.example.userservice.service;

import com.example.userservice.dto.CatalogDto;
import com.example.userservice.jpa.CatalogEntity;
import com.example.userservice.jpa.CatalogRepository;
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

        CatalogEntity catalogEntity = modelMapper.map(catalogDto, CatalogEntity.class);

        CatalogEntity createCatalogEntity = catalogRepository.save(catalogEntity);

        return modelMapper.map(createCatalogEntity,CatalogDto.class);
    }

    public Iterable<CatalogEntity> retrieveCatalog(){
        return catalogRepository.findAll();
    }

}
