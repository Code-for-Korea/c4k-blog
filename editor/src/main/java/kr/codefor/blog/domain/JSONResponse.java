package kr.codefor.blog.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@Getter
public class JSONResponse {
    private final int status;
    private final HashMap<String, Object> data;

    public JSONResponse(HttpStatus httpStatus, HashMap<String, Object> data) {
        this.status = httpStatus.value();
        this.data = data;
    }
}
