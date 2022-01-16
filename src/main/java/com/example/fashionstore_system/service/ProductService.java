package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.ProductImage;
import com.example.fashionstore_system.repository.CategoryRepository;
import com.example.fashionstore_system.repository.ProductImageRepository;
import com.example.fashionstore_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    public Page<Product> listAll(int currentPage, String sortField,
                                 String sortDirection, String keyword, int categoryId) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        if (categoryId == -1) {
            return productRepository.searchProductByName(keyword, pageable);
        }
        return productRepository.searchProductByNameAndCategory(keyword, categoryId, pageable);
    }

    // product_detail
    public Product getProduct(int id) {
        return productRepository.getById(id);
    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public List<ProductImage> findImageProduct() {
        return productImageRepository.findAll();
    }

    //product management
    //search product by name
    public Page<Product> listAll(int currentPage, String keyword) {
        Pageable pageable = PageRequest.of(currentPage - 1, 100);
        if (keyword != null) {
            return productRepository.searchProductByName(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void saveImageProduct(ProductImage productImage) {
        productImageRepository.save(productImage);
    }

    public List<ProductImage> findImageProductById(int id) {
        return productImageRepository.findAllByProductId(id);
    }

    public void deleteProduct(int id) {
        for (ProductImage image : productRepository.getById(id).getProductImages()) {
            productImageRepository.deleteById(image.getId());
        }
        productRepository.deleteById(id);
    }

    public void deleteImageProduct(int id) {
        productImageRepository.deleteById(id);
    }

    public ProductImage findImageById(int id) {
        return productImageRepository.findById(id);
    }
}
