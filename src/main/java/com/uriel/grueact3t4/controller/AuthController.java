
package com.uriel.grueact3t4.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uriel.grueact3t4.dto.AuthRequestDTO;
import com.uriel.grueact3t4.dto.TokenDTO;
import com.uriel.grueact3t4.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService servicio;

    public AuthController(AuthService servicio) {
        this.servicio = servicio;
    }

    // Registro
    @PostMapping("/register")
    public ResponseEntity<TokenDTO> registrar(
            @Valid @RequestBody AuthRequestDTO datos) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(servicio.registrar(datos));
    }

    // Inicio de sesion
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @Valid @RequestBody AuthRequestDTO datos) {

        return ResponseEntity.ok(servicio.login(datos));
    }
}
