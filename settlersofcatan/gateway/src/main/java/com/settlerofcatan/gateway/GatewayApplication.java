package com.settlerofcatan.gateway;

import jwt.JWTUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GatewayApplication {


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


}
