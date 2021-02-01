package kr.codefor.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EditorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EditorApplication.class)
                .properties("" +
                        "spring.config.location=" +
                        "classpath:/application.yml," +
                        "file:C:/repository/_secrets/c4k-blog.yml," +
                        "file:/home/ec2-user/_secrets/c4k-blog.yml"
                )
                .run(args);
    }
}
