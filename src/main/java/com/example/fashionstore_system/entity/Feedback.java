package com.example.fashionstore_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "feedback")
public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
    @Column
    private String content;
    @Column
    private String image;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Transient
    public String getFeedbackImage(){
        if(image==null||id==null) return null;
        return "/feedback_image/" + id + "/" + image;
    }
}
