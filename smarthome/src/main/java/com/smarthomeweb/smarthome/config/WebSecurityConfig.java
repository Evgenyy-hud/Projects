package com.smarthomeweb.smarthome.config;


import com.smarthomeweb.smarthome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){//кодировка пароля и логина
        return new BCryptPasswordEncoder();//BCryptPasswordEncoder используется для шифрования
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()//защита от кросс атак(disable выключить)
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()//регистрироватся могут все
                                .requestMatchers("/").permitAll()//доступ имеют все на основной сайт
                                .requestMatchers("/users").hasRole("ADMIN")//страницв где будет показано пользователи которые зарегестрирова
                ).formLogin(//вход на страницу
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")//по какому url адресу будет размещена страничка логина
                                .defaultSuccessUrl("/users")//метод указывает куда будет переадресован после успешного входа
                                .permitAll()//доступ имеют все
                ).logout(//выход
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//url адрес по которому у нас будут люди выходить
                                .permitAll()//доступ имею все
                );
        return http.build();//
    }

    @Autowired// получение бина
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}