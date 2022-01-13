package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    // sau mỗi trường thêm khoảng trắng để tránh lỗi khi nối chuỗi, đối với trường số thì nên có để so sánh với toán tử "LIKE"
    @Query("SELECT u FROM User u WHERE CONCAT(u.staff.name, ' ', u.staff.email, ' ', u.staff.phone, ' ') LIKE %?1%")
    public List<User> search(String keyword);

    public User findByUsername(String username);
}
