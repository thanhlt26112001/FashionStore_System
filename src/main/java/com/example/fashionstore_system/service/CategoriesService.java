package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Category getById(int id) {
        return categoryRepository.getById(id);
    }

    //phân trang
    public Page<Category> listAllCategory(int currentPage, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        return categoryRepository.findByNameContaining(keyword, pageable);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category findById(int id){
        return categoryRepository.findById(id);
    }


}
