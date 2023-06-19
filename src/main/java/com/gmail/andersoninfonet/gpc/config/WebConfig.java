package com.gmail.andersoninfonet.gpc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class WebConfig {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${server.openapi.url}")
    private String prodUrl;

    @Bean
    public FilterRegistrationBean<RequisicaoFilter> requisicaoFilter() {
        FilterRegistrationBean<RequisicaoFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequisicaoFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource config = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
        corsConfig.setAllowedOrigins(this.allowedOrigins);
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS", "PATCH"));
        config.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(config);
    }

    @Bean
    public OpenAPI openApi() {
        Server server = new Server();
        server.setUrl(prodUrl);
        server.description("Url do servidor");

        Contact contact = new Contact();
        contact.setEmail("bezkoder@gmail.com");
        contact.setName("BezKoder");
        contact.setUrl("https://www.bezkoder.com");

        Info info = new Info()
                .title("Gestão de pessoas e contatos")
                .version("0.o.2")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }

}
