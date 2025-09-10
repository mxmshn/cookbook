package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegistrationUserDto dto);

    UserResponseDto toResponseDto(User user);
}
