package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Feedback;
import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.entity.Promotion;
import com.example.fashionstore_system.entity.Staff;
import com.example.fashionstore_system.repository.FeedbackRepository;
import com.example.fashionstore_system.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

//    public List<Feedback> listAllFeedback() {
//        return feedbackRepository.findAll();
//    }

    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    public Feedback getFeedback(int id) {
        return feedbackRepository.findById(id).get();
    }
    public Feedback getById(int id){ return feedbackRepository.getById(id);}

    public void deleteFeedback(int id) {
        feedbackRepository.deleteById(id);
    }
    //phân trang
    public Page<Feedback> listAllFeedback(int productId, int currentPage) {

        Pageable pageable = PageRequest.of(currentPage - 1, 6);
        return feedbackRepository.findAllByProductId(productId, pageable);
    }
    public List<Feedback> finAllCustomerFeedback(int customerId) {
        return  feedbackRepository.findAllByCustomerId(customerId);
    }


}
