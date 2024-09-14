package ru.obozhulkin.Onlineshop.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;

/**
 * Конфигурация безопасности для приложения.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** Сервис для работы с пользователями. */
    private final PersonDetailsService personDetailsService;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param personDetailsService Сервис для работы с пользователями.
     */
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    /**
     * Конфигурирует HTTP-безопасность.
     *
     * @param http Объект HttpSecurity для конфигурации.
     * @throws Exception Если возникает ошибка при конфигурации.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/adminPage").hasRole("ADMIN")
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                .antMatchers("/api/**").hasRole("ADMIN") // Разрешаем доступ к API для пользователей с ролью ADMIN
                .anyRequest().hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/user/hello", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login")
                .and()
                .httpBasic()
                .and()
                .csrf().ignoringAntMatchers("/api/**");
    }

    /**
     * Конфигурирует менеджер аутентификации.
     *
     * @param auth Объект AuthenticationManagerBuilder для конфигурации.
     * @throws Exception Если возникает ошибка при конфигурации.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
    }

    /**
     * Возвращает кодировщик паролей.
     *
     * @return Кодировщик паролей.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}