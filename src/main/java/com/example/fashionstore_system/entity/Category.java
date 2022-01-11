package com.example.fashionstore_system.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
    @JsonManagedReference
    @OneToMany(mappedBy="category")
    private Set<Product> products;
}