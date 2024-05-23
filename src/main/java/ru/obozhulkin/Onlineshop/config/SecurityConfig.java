package ru.obozhulkin.Onlineshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;
@Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }
@Override
protected void configure(HttpSecurity http) throws Exception{

    //Конфигурируем Security
    http
            .authorizeRequests()
            .antMatchers("/admin/adminPage").hasRole("ADMIN")
            .antMatchers("/auth/login","/auth/registration","/error").permitAll()
            .anyRequest().hasAnyRole("ADMIN","USER")
            .and()
            //Конфигурируем авторизацию
            .formLogin().loginPage("/auth/login")
            .loginProcessingUrl("/process_login")
            .defaultSuccessUrl("/user/hello", true)
            .failureUrl("/auth/login?error")
            .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");
}
    //Настраевает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
}
@Bean
public PasswordEncoder getPasswordEncoder(){
    return new BCryptPasswordEncoder();
}
}
