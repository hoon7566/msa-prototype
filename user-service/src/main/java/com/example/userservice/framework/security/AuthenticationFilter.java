package com.example.userservice.framework.security;

import com.example.userservice.domain.user.UserDto;
import com.example.userservice.domain.user.UserEntity;
import com.example.userservice.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super(authenticationManager);
        this.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class RequestLogin{
        @NotNull
        private String userId;
        @NotNull
        private String password;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(),RequestLogin.class);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserId(),creds.getPassword(),
                            new ArrayList<>()
                    );
            if(request.getCookies() !=null){
                Arrays.stream(request.getCookies()).forEach(cookie -> {
                    log.info(cookie.getName());
                });
            }


            return getAuthenticationManager().authenticate(token);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String userId = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDtoByUserId(userId);

        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();

        String token2 = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong("1111111111111")))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
        Cookie cookieAccessToken = new Cookie("access-token",token);
        Cookie cookieRefreshToken = new Cookie("refresh-token",token2);
        //cookieToken.setHttpOnly(true);
        response.addCookie(cookieAccessToken);
        response.addCookie(cookieRefreshToken);
        response.setHeader("userId",userDto.getUserId());
    }
}
