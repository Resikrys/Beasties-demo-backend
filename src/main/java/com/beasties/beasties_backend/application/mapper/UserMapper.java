package com.beasties.beasties_backend.application.mapper;

import com.beasties.beasties_backend.application.dto.UserDTO;
import com.beasties.beasties_backend.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()))")
    UserDTO toDto(User user);
}
