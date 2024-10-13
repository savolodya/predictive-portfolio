package com.savolodya.predictiveportfolio.models.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus {

    ACTIVE,
    PENDING_REGISTER,
    INVITED,
    LOCKED
}
