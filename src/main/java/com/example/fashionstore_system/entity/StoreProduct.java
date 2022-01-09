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
public class StoreProduct implements Serializable {
    @EmbeddedId
    private StoreProductKey id;
    @ManyToOne
    @MapsId("store_id")
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    private Product product;
    @Column
    private int quantity;
    @Column
    private int status;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    @OneToMany(mappedBy="storeProduct")
    private Set<OrderDetail> orderDetails;
}

