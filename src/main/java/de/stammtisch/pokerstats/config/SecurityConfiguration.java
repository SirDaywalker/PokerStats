package de.stammtisch.pokerstats.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(@NonNull HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                           "/",
                           "/login",
                           "/register",
                           "/request-password-reset",
                           "/request-confirmation",
                           "/error",
                           "/statistics",
                           "/api/v1/auth/register",
                           "/api/v1/auth/authenticate",
                           "/confirm",
                           "/confirm-redirect",
                           "/password-reset",
                            "/password-reset-form",
                           "/api/v1/auth/request-password-reset",
                           "/api/v1/auth/request-confirmation",
                           "/cdn/**",
                           "/js/**",
                           "/css/**",
                           "/img/**",
                           "/assets/**"
                    ).permitAll();
                    auth.requestMatchers(
                            "/users"
                    ).hasAnyAuthority("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(this.authenticationProvider)
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.loginPage("/login"))
                .build();
    }
}
