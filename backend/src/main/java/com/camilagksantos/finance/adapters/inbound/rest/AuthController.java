package com.camilagksantos.finance.adapters.inbound.rest;

import com.camilagksantos.finance.application.dto.request.RegisterRequest;
import com.camilagksantos.finance.application.dto.response.AuthResponse;
import com.camilagksantos.finance.domain.model.UserCredentials;
import com.camilagksantos.finance.domain.ports.input.UserCredentialsUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserCredentialsUseCase userCredentialsUseCase;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(UserCredentialsUseCase userCredentialsUseCase,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtEncoder jwtEncoder) {
        this.userCredentialsUseCase = userCredentialsUseCase;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        UserCredentials credentials = new UserCredentials(
                null,
                request.username(),
                passwordEncoder.encode(request.password()),
                request.userId(),
                null
        );
        userCredentialsUseCase.register(credentials);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody RegisterRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Instant now = Instant.now();
        long expiry = 3600L;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", expiry));
    }
}