package org.iclass.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS=3600;

    // 다른 도메인에서 서버의 자원을 요구할 때 허용해 주는 cors 헤더값 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173", "http://192.168.0.25:5173")
        .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
        //.allowedHeaders("Authorization", "Content-Type")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);

    }

}
