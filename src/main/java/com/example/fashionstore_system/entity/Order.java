package com.example.fashionstore_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone")
    private Integer receiverPhone;
    @Column(name = "receiver_address")
    private String receiverAddress;
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="promotion_id", nullable=true)
    private Promotion promotion;
    @ManyToOne
    @JoinColumn(name="shipping_unit_id", nullable=false)
    private ShippingUnit shippingUnit;
    @Column
    private BigDecimal price;
    @Column(name = "payment_method")
    private Integer paymentMethod;
    //0:ship cod, 1:banking
    @Column
    private Integer status;
    //0:processing, 1: shipping, 2: finished, 3:canceled
    @Column(name = "payment_status")
    private Integer paymentStatus;
    //0:unpaid, 1: paid
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    @OneToMany(mappedBy="order")
    private Set<OrderDetail> orderDetails;

}
