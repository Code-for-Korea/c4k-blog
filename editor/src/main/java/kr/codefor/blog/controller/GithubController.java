package kr.codefor.blog.controller;

import kr.codefor.blog.domain.TokenRefreshVO;
import kr.codefor.blog.domain.JSONResponse;
import kr.codefor.blog.domain.Session;
import kr.codefor.blog.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubController {

    private final SessionService sessionService;

    @Value("${api.github.clientid}")
    private String client_id;

    @Value("${api.github.clientSecret}")
    private String clientSecret;

    @PostMapping("/create-test")
    public Map<String, Object> PutTest() {
        HashMap<String, Object> result = new HashMap<String, Object>();


        String url = "https://api.github.com/repos/Code-for-Korea/c4k-blog/contents/test2.txt";

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/vnd.github.v3+json");
        headers.set("Authorization", "token c8b0c1ea13e9e4e2c667586fc698f8cf41b412e1");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "TEST");
        responseBody.put("content", "dGVzdDENCnRlc3QyDQp0ZXN0Mw==");

        HttpEntity<Map> requestEntity = new HttpEntity<Map>(responseBody, headers);
        HttpEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.PUT, requestEntity, Map.class, responseBody);


        result.put("header", response.getHeaders()); //헤더 정보 확인
        result.put("body", response.getBody()); //실제 데이터 정보 확인

        return result;
    }

    @PostMapping("/delete-test")
    public Map<String, Object> DeleteTest() {
        HashMap<String, Object> result = new HashMap<String, Object>();


        String url = "https://api.github.com/repos/Code-for-Korea/c4k-blog/contents/test2.txt";

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/vnd.github.v3+json");
        headers.set("Authorization", "token c8b0c1ea13e9e4e2c667586fc698f8cf41b412e1");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "TEST");
        responseBody.put("sha", "d79621ca777e294e544143e5fca1980be2084b7a");

        HttpEntity<Map> requestEntity = new HttpEntity<Map>(responseBody, headers);
        HttpEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.DELETE, requestEntity, Map.class, responseBody);


        result.put("header", response.getHeaders()); //헤더 정보 확인
        result.put("body", response.getBody()); //실제 데이터 정보 확인

        return result;
    }

//    @GetMapping("/authorize")
//    public Map<String, Object> ValidAuthorize(@RequestParam String sessionId) {
//
//    }

    @GetMapping("/oauth")
    public JSONResponse CodeToAccessToken(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://github.com/login/oauth/access_token";

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        MultiValueMap<String, String> responseBody = new LinkedMultiValueMap<String, String>();
        responseBody.add("client_id", client_id);
        responseBody.add("client_secret", clientSecret);

        responseBody.add("code", code);

        HashMap resultMap = restTemplate.postForObject(uri.toString(), responseBody, HashMap.class);

        if (resultMap.get("error") == null) {
            Long saveId = sessionService.save(
                    Session.create(
                            resultMap.get("access_token").toString(),
                            resultMap.get("refresh_token").toString()
                    )
            );
            HashMap<String, Object> result = new HashMap<>();
            Session one = sessionService.findOne(saveId);
            result.put("session_id", one.getSessionId());
            result.put("refresh_token", one.getRefreshToken());
            return new JSONResponse(HttpStatus.OK, result);
        }
        return new JSONResponse(HttpStatus.BAD_REQUEST, resultMap);
    }

    @PostMapping("/oauth")
    public JSONResponse RenewingAccessTokenWithRefreshToken(@RequestBody TokenRefreshVO tokenRefreshVO) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://github.com/login/oauth/access_token";

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

        Session one = sessionService.findOne(tokenRefreshVO.getSession_id());

        MultiValueMap<String, String> responseBody = new LinkedMultiValueMap<String, String>();
        responseBody.add("client_id", client_id);
        responseBody.add("client_secret", clientSecret);
        responseBody.add("refresh_token", tokenRefreshVO.getRefresh_token());
        responseBody.add("grant_type", tokenRefreshVO.getGrant_type());

        HashMap resultMap = restTemplate.postForObject(uri.toString(), responseBody, HashMap.class);

        if (resultMap.get("error") == null) {
            sessionService.renewAccessToken(
                    one.getId(),
                    resultMap.get("access_token").toString(),
                    resultMap.get("refresh_token").toString()
            );

            HashMap<String, Object> result = new HashMap<>();
            result.put("session_id", one.getSessionId());
            result.put("refresh_token", one.getRefreshToken());
            return new JSONResponse(HttpStatus.OK, result);
        }
        return new JSONResponse(HttpStatus.BAD_REQUEST, resultMap);
    }
}
