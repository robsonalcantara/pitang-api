package com.pitang.desafiopitangapi.domain.mapper;


import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO toUserDTO(User user);
	User toUserEntity(UserDTO dto);
}