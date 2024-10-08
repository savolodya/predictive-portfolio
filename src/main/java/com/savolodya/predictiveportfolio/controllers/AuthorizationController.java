package com.savolodya.predictiveportfolio.controllers;

import com.savolodya.predictiveportfolio.models.login.LoginAccountForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthorizationController {

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginAccountForm form,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok().build();
    }
}
