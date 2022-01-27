package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.dto.PromotionsDTO;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
//    @Query(value ="select new com.example.fashionstore_system.dto.PromotionsDTO(p.id, p.name, p.discount, p.applyDay, count(o.id)) from Promotion  p left join Order o on p.id = o.promotion.id group by p.id")
//    public Page<PromotionsDTO> listAllPromotions(Pageable pageable);

    public Promotion findByName(String name);

    public Page<Promotion> findByNameContaining(String keyword, Pageable pageable);

    public Promotion findByCode(String Code);

}
