package com.example.userservice.domain.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity , Long> {

    Optional<UserEntity> findByUserId(String userId);
}
