package ru.mashnin.service;

import ru.mashnin.dto.request.RegistrationRequest;

public interface RegistrationService {
    void beginRegistration(RegistrationRequest userDto);
    void completeRegistration(String confirmationToken);
}
