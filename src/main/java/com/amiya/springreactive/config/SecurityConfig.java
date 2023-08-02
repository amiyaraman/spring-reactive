package com.amiya.springreactive.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.netty.http.server.HttpServerState;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.csrf().disable().authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer().jwt(Customizer.withDefaults());

//        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable).authorizeExchange(
//                authorizeExchangeSpec -> authorizeExchangeSpec
//                .anyExchange()
//                .authenticated()
//                ).oauth2ResourceServer(oAuth2ResourceServerSpec ->
//                oAuth2ResourceServerSpec
//                .jwt(Customizer.withDefaults())
//        );
        return serverHttpSecurity.build();
    }
}
