package com.timora.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ AGREGAR TUS DOMINIOS DE PRODUCCIÓN
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",                      // Desarrollo local
                "https://timora-site.netlify.app",            // Tu frontend en Netlify
                "https://timora.netlify.app",                 // Alternativa si usas otro dominio
                "https://timora-front.netlify.app"            // Si tienes otro nombre
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // Tiempo de caché para preflight (opcional)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}