package com.example.fashionstore_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
	private Integer id;
	private String userName;
	private String email;
}
