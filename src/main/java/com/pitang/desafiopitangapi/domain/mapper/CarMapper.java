package com.pitang.desafiopitangapi.domain.mapper;

import com.pitang.desafiopitangapi.domain.dto.CarDTO;
import com.pitang.desafiopitangapi.domain.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
	CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

	CarDTO toCarDTO(Car car);

	Car toCarEntity(CarDTO carDTO);

}
