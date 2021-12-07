package com.example.userservice.domain.user;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class UserEntity {


    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long id;

    @Column(nullable = false , length = 50, unique = true)
    private String userId;

    @Column(nullable = false , length = 50)
    private String name;

    private String encryptedPwd;

    //TODO:JPA Auddit 사용하여 등록자,수정자 넣어보기

    public void modifyName(String name) {
        this.name = name;
    }

}

