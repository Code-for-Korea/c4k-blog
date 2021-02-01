package kr.codefor.blog.service;

import kr.codefor.blog.domain.Session;
import kr.codefor.blog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    @Transactional
    public Long save(Session session) {
        return sessionRepository.save(session);
    }

    @Transactional
    public void renewAccessToken(Long id, String accessToken, String refreshToken) {
        Session one = sessionRepository.findOne(id);
        one.renewAccessToken(
                accessToken,
                refreshToken
        );
    }

    public Session findOne(Long id) {
        return sessionRepository.findOne(id);
    }

    public Session findOne(String session_id) {
        return sessionRepository.findBySessionId(session_id);
    }

}
