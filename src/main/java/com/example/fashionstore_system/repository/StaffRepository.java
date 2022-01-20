package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    public Page<Staff> findByNameContaining(String keyword, Pageable pageable);
    public Staff findByEmail(String email);
}
