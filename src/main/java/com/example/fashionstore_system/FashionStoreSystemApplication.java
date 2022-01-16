package com.example.fashionstore_system;

import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FashionStoreSystemApplication  {
    public static void main(String[] args) {
        SpringApplication.run(FashionStoreSystemApplication.class, args
        );
    }
}
