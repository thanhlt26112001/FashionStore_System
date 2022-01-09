package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.ShippingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingUnitRepository extends JpaRepository<ShippingUnit, Integer> {
}
