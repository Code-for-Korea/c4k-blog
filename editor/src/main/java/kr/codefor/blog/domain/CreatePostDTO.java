package kr.codefor.blog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreatePostDTO {
    private final PostVO post;
    private final AuthenticateVO authenticate;
}
