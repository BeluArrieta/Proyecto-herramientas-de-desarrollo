package com.example.proyec_herramientas.controller;

import com.example.proyec_herramientas.dto.AuthRequest;
import com.example.proyec_herramientas.dto.LoginResponse;
import com.example.proyec_herramientas.dto.RegistroRequest;
import com.example.proyec_herramientas.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        LoginResponse respuesta = authService.login(request);
        if (respuesta == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthService.error("Usuario o contrasena incorrectos"));
        }
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            LoginResponse respuesta = authService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(AuthService.error(e.getMessage()));
        }
    }
}