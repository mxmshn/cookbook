package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.RegistrationData;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegistrationRequest dto);

    UserResponseDto toResponseDto(User user);
    RegistrationData toRegistrationData(User user);
}
