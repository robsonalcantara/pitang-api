package com.pitang.desafiopitangapi.config;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private static final String[] AUTH_WHITELIST = {
    		"/api/v1/auth/**",
    		"/v3/api-docs/**",
    		"/configuration/ui", 
    		"/swagger-resources/**", 
    		"/swagger-ui.html", 
    		"/webjars/**", 
    		"/swagger-ui/**"    
    		};
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable).headers(httpSecurityHeadersConfigurer -> {
        			httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
        		})
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
						.requestMatchers(HttpMethod.POST, "/api/signin").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/users").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()
						.requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
						.requestMatchers(toH2Console()).permitAll()
                        .anyRequest().authenticated())
        ;
        return http.build();
    }
}
