package kr.codefor.blog.domain;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Getter
public class PostVO {
    private final String title;
    private final String content;
    private final String author;
    private final List<String> categories;
    private final List<String> tags;

    public PostVO(String title, String author, List<String> categories, List<String> tags, String content) {
        this.title = title;
        this.author = author;
        this.categories = categories;
        this.tags = tags;

        String msg = "" +
                "---\n" +
                "title: " + title + "\n" +
                "author: " + author + "\n" +
                "date: " + ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) + " +0900\n" +
                "categories: " + categories + "\n" +
                "tags: " + tags + "\n" +
                "---\n" +
                content;

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(msg.getBytes(StandardCharsets.UTF_8));
        this.content = new String(encodedBytes);
    }
}
