package com.example.userservice.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public UserEntity createUser(UserDto userDto){

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        UserEntity createUserEntity = userRepository.save(userEntity);

        return createUserEntity;
    }

    public Iterable<UserEntity> retrieveUser(){
        return userRepository.findAll();
    }

    public UserEntity getUserDtoByUserId(String userId)  {
        return userRepository.findByUserId(userId).orElseThrow( () -> new UserNotFoundException("사용자 없음. "+ userId ));
    }

    public void updateUser(UserDto userDto){
        UserEntity userEntity = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자 없음. "+ userDto.getUserId()));
        userEntity.setName(userDto.getName());
        userRepository.save(userEntity);
    }

    public void deleteUser(UserDto userDto){
        userRepository.delete(
                userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자 없음. "+ userDto.getUserId() ))
        );
    }

    @Override
    public UserDetails loadUserByUsername(String userId)  {

        return userRepository.findByUserId(userId)
                .map( userEntity -> {return new User(userEntity.getUserId(),userEntity.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());}).orElseThrow( ()-> new UserNotFoundException("사용자없음: " + userId));
    }
}
