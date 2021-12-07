package com.example.userservice.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;




    @Test
    @DisplayName("유저생성_성공")
    void createUser_Success() throws Exception {

        //given
        UserController.RequestUser requestUser =
                UserController.RequestUser.builder()
                        .userId("hoon7566")
                        .name("hoony")
                        .password("test1234")
                        .build();

        UserDto userDto =
                UserDto.builder()
                        .userId("hoon7566")
                        .name("hoony")
                        //.password("test1234")
                        .build();

        BDDMockito.given(userService.createUser(userDto)).willReturn(userDto);

        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content(objectMapper.writeValueAsString(requestUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));


        //then
        actions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("userId").value("hoon7566"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void retrieveUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}