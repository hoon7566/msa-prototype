package com.example.catalogservice.service;

import com.example.catalogservice.dto.UserDto;
import com.example.catalogservice.jpa.UserEntity;
import com.example.catalogservice.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public UserDto createUser(UserDto userDto){

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        UserEntity createUserEntity = userRepository.save(userEntity);

        return modelMapper.map(createUserEntity,UserDto.class);
    }

    public Iterable<UserEntity> retrieveUser(){
        return userRepository.findAll();
    }

}
