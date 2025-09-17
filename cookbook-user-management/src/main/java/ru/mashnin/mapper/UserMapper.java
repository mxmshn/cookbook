package ru.mashnin.mapper;

import org.mapstruct.Mapper;
import ru.mashnin.dto.RegistrationData;
import ru.mashnin.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegistrationData registrationData);
}
