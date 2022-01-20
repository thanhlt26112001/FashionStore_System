package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Promotion;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer findByEmail(String email){
        return customerRepository.findByEmail(email);

    }
    public Customer getById(int id){
        return customerRepository.getById(id);
    }

    //anhht made this
//    public List<Customer> listAll(String keyword) {
//        if (keyword != null) {
//            return customerRepository.searchCustomerByName(keyword);
//        }
//        return customerRepository.findAll();
//    }

    public Customer get(int id) {
        return customerRepository.findById(id).get();
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }


    //phân trang
    public Page<Customer> listAllCustomer(int currentPage, String sortField, String sortDirection, String keyword) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currentPage - 1, 6, sort);
        return customerRepository.findByNameContaining(keyword, pageable);
    }
    public List<Customer> getAllCustomer(){
        return customerRepository.findAll();
    }

}
