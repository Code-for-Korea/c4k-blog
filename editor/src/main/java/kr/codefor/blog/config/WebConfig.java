package kr.codefor.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${environment.launch-mode}")
    private String launchMode;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (launchMode.equals("dev")) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4000");
        } else if (launchMode.equals("production")) {
            registry.addMapping("/**")
                    .allowedOrigins("https://blog.codefor.kr");
        }
    }
}
