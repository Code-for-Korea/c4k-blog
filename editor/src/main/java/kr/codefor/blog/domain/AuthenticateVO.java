package kr.codefor.blog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthenticateVO {
    private final String session_id;
    private final String refresh_token;
    private final String grant_type;

}
