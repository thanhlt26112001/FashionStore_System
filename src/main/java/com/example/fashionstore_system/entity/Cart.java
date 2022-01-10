package com.example.fashionstore_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name="store_product_id", nullable=false)
    @JoinColumn(name="product_id", nullable=false)
    private StoreProduct storeProduct;
    @ManyToOne
    @JoinColumn(name="size_product_id", nullable=false)
    @JoinColumn(name="size_id", nullable=false)
    private SizeProduct sizeProduct;
    @Column
    private Integer quantity;
}
