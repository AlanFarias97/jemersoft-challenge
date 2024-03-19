package com.challenge.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Permitir todas las fuentes
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permitir métodos específicos
                .allowedHeaders("*"); // Permitir todos los encabezados
    }
}
