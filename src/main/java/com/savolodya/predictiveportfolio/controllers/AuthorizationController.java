package com.savolodya.predictiveportfolio.controllers;

import com.savolodya.predictiveportfolio.models.login.LoginAccountForm;
import com.savolodya.predictiveportfolio.models.register.RegisterAccountForm;
import com.savolodya.predictiveportfolio.services.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PutMapping("/register")
    public ResponseEntity<Void> startRegisterAccountAction(
            @RequestBody RegisterAccountForm form
    ) {
        authorizationService.createRegisterAccountAction(form.email());
        return ResponseEntity.created(URI.create("")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginAccountForm form,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok().build();
    }
}
