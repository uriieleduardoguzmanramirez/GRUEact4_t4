
package com.uriel.grueact3t4.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uriel.grueact3t4.dto.AuthRequestDTO;
import com.uriel.grueact3t4.dto.TokenDTO;
import com.uriel.grueact3t4.model.Usuario;
import com.uriel.grueact3t4.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UsuarioRepository usuarios,
                       PasswordEncoder encoder,
                       JwtEncoder jwtEncoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwtEncoder = jwtEncoder;
    }

    // Registrar usuario
    @Transactional
    public TokenDTO registrar(AuthRequestDTO datos) {

        String email = datos.email().trim()
                .toLowerCase(Locale.ROOT);

        if (usuarios.existsByEmail(email)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "El correo ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPasswordHash(
            encoder.encode(datos.password()));

        usuarios.save(usuario);

        return generarToken(email);
    }

    // Iniciar sesion
    public TokenDTO login(AuthRequestDTO datos) {

        String email = datos.email().trim()
                .toLowerCase(Locale.ROOT);

        Usuario usuario = usuarios.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        if (!encoder.matches(
                datos.password(), usuario.getPasswordHash())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        return generarToken(email);
    }

    // Generar JWT firmado
    private TokenDTO generarToken(String email) {

        Instant ahora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("grueact4t4")
            .subject(email)
            .issuedAt(ahora)
            .expiresAt(ahora.plus(1, ChronoUnit.HOURS))
            .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
            .build();

        String token = jwtEncoder.encode(
            JwtEncoderParameters.from(header, claims))
            .getTokenValue();

        return new TokenDTO(token, "Bearer");
    }
}
