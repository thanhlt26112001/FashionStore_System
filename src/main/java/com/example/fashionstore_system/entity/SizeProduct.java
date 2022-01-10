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
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "store_product")
public class SizeProduct implements Serializable {
    @EmbeddedId
    private SizeProductKey id;
    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @MapsId("size_id")
    @JoinColumn(name = "size_id")
    private Size size;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    @OneToMany(mappedBy="sizeProduct")
    private Set<OrderDetail> orderDetails;
    @OneToMany(mappedBy="sizeProduct")
    private Set<Cart> carts;
}
