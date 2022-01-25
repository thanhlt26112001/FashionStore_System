package com.example.fashionstore_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String description;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column
    private String image;
    @Column
    private BigDecimal price;
    @Column
    private Integer quantity;
    @Column
    private Integer status;
    //0: out of stock, 1: available
    @Column
    private Integer count;
    // count product order
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    @OneToMany(mappedBy = "product")
    private Set<Feedback> feedbacks;
    @OneToMany(mappedBy = "product")
    private Set<ProductImage> productImages;
    @OneToMany(mappedBy="product")
    private Set<Cart> carts;
    @OneToMany(mappedBy="order")
    private Set<OrderDetail> orderDetails;


}