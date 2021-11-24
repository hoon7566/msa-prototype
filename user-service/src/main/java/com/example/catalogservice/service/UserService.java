package com.example.catalogservice.service;

import com.example.catalogservice.dto.UserDto;
import com.example.catalogservice.jpa.UserEntity;
import com.example.catalogservice.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    public UserDto getUserDtoByUserId(String userId) throws Exception {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity ==null) throw new UserNotFoundException("User Not Found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;

    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUserId(userId);

        return new User(userEntity.getUserId(),userEntity.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }
}
