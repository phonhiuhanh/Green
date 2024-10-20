package com.poly.greeen.Controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests() // Đổi thành authorizeHttpRequests()
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/assets/**").permitAll() // Cho phép các request này không cần đăng nhập
                .anyRequest().authenticated() // Các request khác yêu cầu đăng nhập
                .and()
                .formLogin()
                .loginPage("/login") // Đường dẫn tới trang đăng nhập
                .defaultSuccessUrl("/giaodien/shop", true) // Chuyển hướng sau khi đăng nhập thành công
                .failureUrl("/login?error=true") // Chuyển hướng nếu đăng nhập thất bại
                .permitAll()
                .loginPage("/login") // Đường dẫn trang đăng nhập tùy chỉnh
                .defaultSuccessUrl("/giaodien/shop", true) // Đăng nhập thành công chuyển tới đây
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // URL logout
                .logoutSuccessUrl("/login?logout") // Sau khi logout chuyển về trang login
                .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu bằng BCrypt
    }
}
