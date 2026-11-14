package ru.student.grants.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.student.grants.dto.AuthResponse;
import ru.student.grants.model.SessionStatus;
import ru.student.grants.model.User;
import ru.student.grants.model.UserSession;
import ru.student.grants.repository.UserRepository;
import ru.student.grants.repository.UserSessionRepository;
import ru.student.grants.security.JwtService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final SessionSecurityService sessionSecurityService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(String username, String rawPassword, String deviceId) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        return issueTokenPair(username, deviceId);
    }

    @Transactional
    public AuthResponse login(String username, String rawPassword, String deviceId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return issueTokenPair(username, deviceId);
    }

    @Transactional
    public AuthResponse issueTokenPair(String username, String deviceId) {
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        Instant accessExpiry = jwtService.extractExpirationInstant(accessToken);
        Instant refreshExpiry = jwtService.extractExpirationInstant(refreshToken);

        UserSession session = UserSession.builder()
                .username(username)
                .deviceId(deviceId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiry(accessExpiry)
                .refreshTokenExpiry(refreshExpiry)
                .status(SessionStatus.ACTIVE)
                .build();

        sessionRepository.save(session);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshTokens(String oldRefreshToken, String deviceId) {
        if (!jwtService.validateToken(oldRefreshToken, "refresh")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }

        // Используем блокировку для защиты от конкурентных запросов
        UserSession session = sessionRepository.findByRefreshTokenWithLock(oldRefreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown refresh session"));

        if (session.getStatus() == SessionStatus.REVOKED || session.getStatus() == SessionStatus.USED) {
            if (session.getStatus() == SessionStatus.USED) {
                // Ловушка ТЗ: повторное предъявление USED токена переводит его в REVOKED
                // и это изменение ДОЖНО сохраниться, несмотря на 401 ошибку!
                sessionSecurityService.forceSaveRevoked(session);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session is already used or revoked");
        }

        if (session.getRefreshTokenExpiry().isBefore(Instant.now())) {
            sessionSecurityService.forceSaveRevoked(session);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        String username = jwtService.extractUsername(oldRefreshToken);
        if (!username.equals(session.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token owner mismatch");
        }

        // Переводим текущую сессию в статус USED
        session.setStatus(SessionStatus.USED);
        sessionRepository.save(session);

        // Выдаем новую пару
        return issueTokenPair(username, deviceId != null ? deviceId : session.getDeviceId());
    }
}