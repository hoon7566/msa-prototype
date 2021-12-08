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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserDto createUser(UserDto userDto){

        UserEntity userEntity = new UserEntity(userDto);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        UserEntity createUserEntity = userRepository.save(userEntity);

        return new UserDto(createUserEntity);
    }

    @Transactional(readOnly = true)
    public List<UserDto> retrieveUser(){
        return userRepository.findAll()
                .stream()
                .map(e->new UserDto(e))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserDtoByUserId(String userId)  {
        return userRepository.findByUserId(userId)
                .map(e->new UserDto(e))
                .orElseThrow( () -> new UserNotFoundException("사용자 없음. "+ userId ));
    }

    public void updateUser(UserDto userDto){
        UserEntity userEntity = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자 없음. "+ userDto.getUserId()));
        userEntity.modifyName(userDto.getName());
    }

    public void deleteUser(UserDto userDto){
        userRepository.delete(
                userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자 없음. "+ userDto.getUserId() ))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId)  {

        return userRepository.findByUserId(userId)
                .map( userEntity -> {return new User(userEntity.getUserId(),userEntity.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());})
                .orElseThrow( ()-> new UserNotFoundException("사용자없음: " + userId));
    }
}
