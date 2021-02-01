package kr.codefor.blog.repository;

import kr.codefor.blog.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

    private final EntityManager em;

    public Long save(Session session) {
        em.persist(session);
        return session.getId();
    }

    public Session findOne(Long sessionId) {
        return em.find(Session.class, sessionId);
    }

    public Session findBySessionId(String sessionId) {
        List<Session> sessions = em.createQuery("" +
                " SELECT session FROM Session session" +
                " WHERE session.sessionId =: sessionId", Session.class)
                .setParameter("sessionId", sessionId)
                .getResultList();

        if(sessions.size() > 0) {
            return sessions.get(0);
        } else {
            return null;
        }
    }
}
