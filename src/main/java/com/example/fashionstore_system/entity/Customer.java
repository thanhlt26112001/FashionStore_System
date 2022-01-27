package com.example.fashionstore_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    @Email
    private String email;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    private Date birthday;
    @Column
    private String avatar;
    @Column
    private Integer point;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    @OneToOne(mappedBy = "customer")
    private User user;
    @OneToMany(mappedBy="customer")
    private Set<Order> orders;
    @OneToMany(mappedBy="customer")
    private Set<Feedback> feedbacks;
    @OneToMany(mappedBy="customer")
    private Set<Cart> carts;
    @Column
    private Integer status;
}
