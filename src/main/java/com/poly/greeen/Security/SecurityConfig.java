package com.poly.greeen.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final CustomUserDetailsService userDetailsService;

    private static final String[] NON_AUTHENTICATED_ENDPOINTS = {
            "/login", "/index/register", "/css/**", "/js/**", "/assets/**", "/api/products/**", "/index/forgot-password", "/p/**", "/api/*/p/**", "/*/p/**"
    };
    private static final String[] IGNORED_ENDPOINTS = {
            "/static/**", "/user/**", "/assets/**", "/assets-admin/**", "/admin/**", "/giaodien/**"
    };
    private static final String[] MANAGER_ENDPOINTS = {
            "/api/*/manager/**", "/*/manager/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(NON_AUTHENTICATED_ENDPOINTS).permitAll()
                        .requestMatchers(MANAGER_ENDPOINTS).hasAuthority(CustomUserDetailsService.ROLES.get("ROLE_MANAGER"))
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll())
                .securityMatcher("/**")
        ;

        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(IGNORED_ENDPOINTS);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        var builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Mã hóa mật khẩu bằng BCrypt
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                var currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String role = currentUser.getAuthUser().getRole();
                log.info("authorities: {}; role: {}", currentUser.getAuthorities().toArray()[0].toString(), currentUser.getAuthUser().getRole());
                String successUrl = "/Admin/trangchu";
                if (role.equals(CustomUserDetailsService.ROLES.get("ROLE_CUSTOMER"))) {
                    successUrl = "/index/shop";
                }
                response.sendRedirect(successUrl);
            }
        };
    }
}
