package br.edu.unidavi.alfonso.springfinal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("barry").password("t0ps3cr3t").roles("VENDEDOR").and()
                .withUser("larry").password("t0ps3cr3t").roles("VENDEDOR", "MANAGER").and()
                .withUser("root").password("t0ps3cr3t").roles("VENDEDOR", "MANAGER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable()
                .requestMatchers()
                    .antMatchers("/login", "/oauth/authorize")
                    //.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**","/swagger-ui.html", "/webjars/**")
                .and()
                    .authorizeRequests()
                        .antMatchers("/pedidos/**").hasAnyRole("VENDEDOR", "MANAGER", "ADMIN")
                        .antMatchers("/clientes/**").hasAnyRole("MANAGER")
                        .antMatchers("/produtos/**").hasAnyRole("MANAGER")
                        .antMatchers("/h2/**").permitAll()
                        .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**","/swagger-ui.html", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                .and()
                    .formLogin().permitAll();
//        http.authorizeRequests()
//                .antMatchers("/pedidos/**").hasAnyRole("VENDEDOR", "MANAGER", "ADMIN")
//                .antMatchers("/clientes/**").hasAnyRole("MANAGER")
//                .antMatchers("/produtos/**").hasAnyRole("MANAGER")
//                .antMatchers("/h2/**").permitAll();
//                .anyRequest().fullyAuthenticated()
//                .and().httpBasic().and().csrf().disable();
    }
}
