package com.example.fashionstore_system.repository;


import com.example.fashionstore_system.dto.OrderDTO;
import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCustomerId(Integer customerId);
    public Page<Order> findByReceiverNameContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM Orders WHERE DATE(created_at) BETWEEN ?1 and ?2 and receiver_name like %?3%", nativeQuery = true)
    public Page<Order> findAllOrderByTime( Date startDate, Date endDate,String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM Orders WHERE DATE(created_at) >= ?1 and receiver_name like %?2%", nativeQuery = true)
    public Page<Order> findStartOrderByTime( Date startDate, String keyword,Pageable pageable);

    @Query(value = "SELECT * FROM Orders WHERE DATE(created_at) <= ?1 and receiver_name like %?2%", nativeQuery = true)
    public Page<Order> findEndOrderByTime( Date endDate,String keyword, Pageable pageable);

    @Query("SELECT s FROM Order s WHERE CONCAT(s.receiverName, ' ') LIKE %?1%")
    public List<Order> search(String keyword);


    @Query(value = "SELECT new com.example.fashionstore_system.dto.OrderDTO(" +
            "o.customer.id, c.name, count(o.id), sum(o.price), o.createdAt )" +
            "FROM Order o join Customer c on o.customer.id = c.id" +
            " group by  o.customer.id, o.createdAt")
    public List<OrderDTO> getRevenueformonth();

    @Query(value = "SELECT * FROM Orders where status = 2 order by id desc limit 7", nativeQuery = true)
    public List<Order> getRecentOrder();

    @Query(value = "SELECT * FROM Orders where Month(created_at) = ?1 and Year(created_at) = ?2", nativeQuery = true)
    public Page<Order> findByMonthAndYear(int month, int year, Pageable pageable);

//    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m-%d') as date from orders where id=6", nativeQuery = true)
//    public Page<Order> findByTime(String startDate, String endDate, Pageable pageable);
}
