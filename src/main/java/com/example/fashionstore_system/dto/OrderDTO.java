package com.example.fashionstore_system.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private int id;
    private String name;
    private Long totalOrer;
    private BigDecimal price;
    private Date createdAt;
}
