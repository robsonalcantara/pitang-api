package com.pitang.desafiopitangapi.domain.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pitang.desafiopitangapi.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

	private String id;
	private int year;
	private String licensePlate;
	private String model;
	private String color;
	private Boolean usage;
	private Integer usageCount;
	@JsonIgnore
	private User user;

	public boolean validate() {
		return Objects.nonNull(color) && Objects.nonNull(year) && Objects.nonNull(model)
				&& Objects.nonNull(licensePlate);
	}
}
