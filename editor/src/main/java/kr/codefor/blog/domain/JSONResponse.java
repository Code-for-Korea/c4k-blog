package kr.codefor.blog.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
public class JSONResponse {
    private final LocalDateTime date;
    private final HashMap<String, Object> data;

    public JSONResponse(HashMap<String, Object> data) {
        this.date = LocalDateTime.now();
        this.data = data;
    }
}
