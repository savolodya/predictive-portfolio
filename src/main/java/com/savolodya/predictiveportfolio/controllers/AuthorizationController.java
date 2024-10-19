package com.savolodya.predictiveportfolio.controllers;

import com.savolodya.predictiveportfolio.models.login.LoginUserForm;
import com.savolodya.predictiveportfolio.models.register.RegisterUserForm;
import com.savolodya.predictiveportfolio.services.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PutMapping("/register")
    public ResponseEntity<Void> startRegisterAccountAction(
            @RequestBody RegisterUserForm form
    ) {
        authorizationService.createRegisterUserAction(form.email(), form.teamName());
        return ResponseEntity.created(URI.create("")).build();
    }

    @PatchMapping("/register/{actionToken}")
    public ResponseEntity<Void> finishRegisterAccountAction(
            @Valid @RequestBody RegisterUserForm form,
            @PathVariable UUID actionToken
    ) {
        authorizationService.finishRegisterUserAction(actionToken, form.password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginUserForm form
    ) {
        authorizationService.authorizeAccount(form.email(), form.password());
        return ResponseEntity.ok().build();
    }
}
