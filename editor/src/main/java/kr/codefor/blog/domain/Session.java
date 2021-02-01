package kr.codefor.blog.domain;

import com.sun.istack.NotNull;
import kr.codefor.blog.util.Util;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "session_id")
    @NotNull
    private String sessionId;

    @Column(name = "access_token")
    @NotNull
    private String accessToken;

    @Column(name = "expires_in")
    @NotNull
    private LocalDateTime expiresIn;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "refresh_token")
    @NotNull
    private String refreshToken;

    public static Session create(String accessToken, String refreshToken) {
        Session session = new Session();
        session.sessionId = GenerateSessionId();
        session.accessToken = accessToken;
        session.expiresIn = LocalDateTime.now().plusSeconds(28800);
        session.createdAt = LocalDateTime.now();
        session.refreshToken = refreshToken;

        return session;
    }

    public void renewAccessToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.expiresIn = LocalDateTime.now().plusSeconds(28800);
        this.createdAt = LocalDateTime.now();
        this.refreshToken = refreshToken;
    }

    private static String GenerateSessionId() {
        Util util = new Util();

        String plainText = "" + System.currentTimeMillis() + "::" + "Code for Korea Blog";
        return util.sha256Encrypt(plainText);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", createdAt=" + createdAt +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
