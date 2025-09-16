package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
}
