package com.example.fashionstore_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionsDTO {
    private Integer id;
    private String name;
    private Integer discount;
    private String applyDay;
    private Long number_of_orders;
}
