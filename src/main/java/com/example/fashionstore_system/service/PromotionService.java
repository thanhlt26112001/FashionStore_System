package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Promotion;
import com.example.fashionstore_system.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    public List<Promotion> listAll() {
        return promotionRepository.findAll();
    }

    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    public void delete(Integer id) {
        promotionRepository.deleteById(id);
    }

    public Promotion getById(int id) {
        return promotionRepository.getById(id);
    }

    //phân trang
    public Page<Promotion> listAllPromotion(int currentPage, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        return promotionRepository.findByNameContaining(keyword, pageable);
    }

    public Promotion findByName(String name) {
        return promotionRepository.findByName(name);
    }
    public Promotion findByCode(String code) {
        return promotionRepository.findByCode(code);
    }

    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }
    public Promotion getPromotionByCode(String promotionCode){
        return promotionRepository.findByCode(promotionCode);
    }
}

