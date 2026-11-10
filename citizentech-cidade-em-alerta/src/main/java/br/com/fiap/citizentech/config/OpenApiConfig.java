package br.com.fiap.citizentech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI citizenTechOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("CitizenTech - Cidade em Alerta")
                .version("1.0.0")
                .description("API para previsão e prevenção de riscos urbanos com Alerta Antecipado por Microrregião."));
    }
}
