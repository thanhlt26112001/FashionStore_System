package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Promotion;
import com.example.fashionstore_system.entity.ShippingUnit;
import com.example.fashionstore_system.repository.ShippingUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingUnitService {
    @Autowired
    ShippingUnitRepository shippingUnitRepository;
    public ShippingUnit getShippingUnitById(int shippingUnitId){
        return shippingUnitRepository.getById(shippingUnitId);
    }
//    public void saveShippingUnit(ShippingUnit shippingUnit) {
//        shippingUnitRepository.saveShippingUnit(shippingUnit);
//    }

    public void saveShippingUnit(ShippingUnit shippingUnit) {
        shippingUnitRepository.save(shippingUnit);
    }
}
