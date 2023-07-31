package com.example.demo.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired
    private MyFilter myFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer ->
                        customizer.requestMatchers("/public/**")
                                .permitAll()
                                .requestMatchers("/admin/**")
                                .hasAuthority("ADMIN")
                                .requestMatchers("/**")
                                .hasAuthority("USER")
                                .anyRequest().permitAll())
                .addFilterBefore(myFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
}
