package br.com.process.integration.database.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Define as rotas que serão liberadas
            .allowedOrigins("http://localhost:4200") // Define a origem permitida (ex: Angular rodando localmente)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Define os métodos HTTP permitidos
            .allowedHeaders("*") // Define os cabeçalhos permitidos
            .allowCredentials(true); // Permite envio de credenciais (cookies, autenticação, etc.)
    }
}
