package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Promotion;
import com.example.fashionstore_system.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }
    public Promotion getPromotionById(int promotionId){
        return promotionRepository.findAllById(promotionId);
    }
}
