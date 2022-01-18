package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.ShippingUnit;
import com.example.fashionstore_system.repository.ShippingUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingUnitService {
    @Autowired
    private ShippingUnitRepository shippingUnitRepository;

    public List<ShippingUnit> getAllShippingUnits() {
        return shippingUnitRepository.findAll();

    }
}
