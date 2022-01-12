package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findUserById(Integer id);
}
