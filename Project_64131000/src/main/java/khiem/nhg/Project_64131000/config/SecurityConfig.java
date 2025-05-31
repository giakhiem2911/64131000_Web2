package khiem.nhg.Project_64131000.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ cho phép ADMIN truy cập vào các URL bắt đầu bằng /admin
                .requestMatchers("/", "/login", "/register", "/css/**", "/images/**", "/uploads/images/**", "/articles/**", "/category/**", "/js/**", "/api/interactions/like").permitAll() // Cho phép tất cả truy cập vào các trang này
                .anyRequest().authenticated() // Tất cả các yêu cầu khác cần xác thực
            )
            .formLogin(form -> form
                .loginPage("/login") // Đường dẫn đến trang đăng nhập
                .loginProcessingUrl("/login") // Đường dẫn xử lý đăng nhập
                .successHandler(customAuthenticationSuccessHandler()) // Xử lý khi đăng nhập thành công
                .permitAll() // Cho phép tất cả truy cập vào trang đăng nhập
            )
            .logout(logout -> logout
            	    .logoutUrl("/logout") // URL thực hiện logout (mặc định là POST)
            	    .logoutSuccessUrl("/login?logout") // Chuyển hướng đến trang đăng nhập với thông báo
            	    .invalidateHttpSession(true) // Xoá session hiện tại
            	    .clearAuthentication(true) // Xoá thông tin xác thực
            	    .deleteCookies("JSESSIONID") // Xoá cookie JSESSIONID
            	    .permitAll()
            	);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(); // Xử lý tùy chỉnh khi đăng nhập thành công
    }
}
