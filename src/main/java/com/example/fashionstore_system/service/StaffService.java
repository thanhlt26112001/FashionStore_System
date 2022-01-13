package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    @Autowired
    private StaffRepository repo;

    public List<Staff> listAll() {
        return repo.findAll();
    }

    public void save(Staff staff) {

        repo.save(staff);
    }

    public Staff get(int id) {

        return repo.findById(id).get();
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

    // Staff search function by keyword
    public List<Staff> listAll(String keyword){
        if (keyword !=null){
            return repo.search(keyword);
        }
        return repo.findAll();
    }
}
