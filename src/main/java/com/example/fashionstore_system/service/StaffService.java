package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> listAll() {
        return staffRepository.findAll();
    }

    public Staff getById(int id) {
        return staffRepository.getById(id);
    }

    public Optional<Staff> findById(int id){
        return staffRepository.findById(id);
    }

    public void saveStaff(Staff staff) {
        staffRepository.save(staff);
    }

    public Staff get(int id) {
        return staffRepository.findById(id).get();
    }

    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    public void delete(int id) {
        staffRepository.deleteById(id);
    }

    public Page<Staff> listAllStaff(int currentPage, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        return staffRepository.findByNameContaining(keyword, pageable);
    }
}
