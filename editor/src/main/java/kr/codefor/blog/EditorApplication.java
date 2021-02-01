package kr.codefor.blog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EditorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EditorApplication.class)
                .properties("" +
                        "spring.config.location=" +
                        "classpath:/application.yml," +
                        "optional:C:/repository/_secrets/c4k-blog.yml," +
                        "optional:/home/ec2-user/_secrets/c4k-blog.yml"
                )
                .run(args);
    }
}
