package kr.codefor.blog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenRefreshVO {
    private String session_id;
    private String refresh_token;
    private String grant_type;

}
