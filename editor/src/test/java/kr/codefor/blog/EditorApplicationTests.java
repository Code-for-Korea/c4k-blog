package kr.codefor.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "" +
        "spring.config.location=" +
        "classpath:/application.yml," +
        "optional:C:/repository/_secrets/c4k-blog.yml," +
        "optional:/home/ec2-user/_secrets/c4k-blog.yml")
class EditorApplicationTests {

    @Test
    void contextLoads() {
    }

}
