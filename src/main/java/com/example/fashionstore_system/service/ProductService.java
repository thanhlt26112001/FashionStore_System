package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.repository.CategoryRepository;
import com.example.fashionstore_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

//phan trang
//public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
//    Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
//            Sort.by(sortField).descending();
//
//    Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
//    return this.productRepository.findAll(pageable);
//}
//
//    public Page<Product> findAll(Pageable pageable) {
//        return productRepository.findAll(pageable);
//    }
    public Page<Product> listAll(int currentPage, String sortField, String sortDirection, String keyword, int categoryId){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
    Pageable pageable = PageRequest.of(currentPage - 1,6, sort);
        if(categoryId==-1){
            return productRepository.searchProductByName(keyword, pageable);
        }
        return productRepository.searchProductByNameAndCategory(keyword, categoryId, pageable);
}
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
public Page<Product> listAll(int currentPage, String keyword){

    Pageable pageable = PageRequest.of(currentPage - 1,6);
    if (keyword != null) {
        return this.productRepository.searchProductByName(keyword, pageable);
    }
    return this.productRepository.findAll(pageable);

}
    // product_detail
    public Product getProduct(int id) {
        return productRepository.getById(id);
    }

    //search product by name
//    public List<Product> listAll(String keyword) {
//        if (keyword != null) {
//            return productRepository.searchProductByName(keyword);
//        }
//        return productRepository.findAll();
//    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }
}