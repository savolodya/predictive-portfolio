package com.savolodya.predictiveportfolio.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String identifier) {
        super("User not found");
        log.error("User with identifier [{}] not found", identifier);
    }
}
