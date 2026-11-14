package ru.student.grants.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.student.grants.model.SessionStatus;
import ru.student.grants.model.UserSession;
import ru.student.grants.repository.UserSessionRepository;

@Service
@RequiredArgsConstructor
public class SessionSecurityService {

    private final UserSessionRepository sessionRepository;

    // Эта транзакция изолирована и фиксируется сразу, даже если основной метод выбросит 401
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void forceSaveRevoked(UserSession session) {
        session.setStatus(SessionStatus.REVOKED);
        sessionRepository.save(session);
    }
}