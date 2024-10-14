package com.savolodya.predictiveportfolio.exceptions;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Slf4j
public class ActionTokenNotFoundException extends RuntimeException {

    public ActionTokenNotFoundException() {
        super("Action Token not found");
    }

    public ActionTokenNotFoundException(ActionTokenType type, UUID token) {
        super("Action Token not found");
        log.error("{} action Token [{}] not found", type.name(), token);
    }
}
