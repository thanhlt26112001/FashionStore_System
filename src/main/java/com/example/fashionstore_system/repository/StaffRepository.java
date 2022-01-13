package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    // sau mỗi trường thêm khoảng trắng để tránh lỗi khi nối chuỗi, đối với trường số thì nên có để so sánh với toán tử "LIKE"
    @Query("SELECT s FROM Staff s WHERE CONCAT(s.name, ' ', s.email, ' ', s.phone) LIKE %?1%")
    public List<Staff>  search(String keyword);

}
