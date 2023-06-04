package com.kaci.blogspring0505.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity() // pour méthode 2
public class SecurityConfig {

    /* Encodage *********************/
    //Encodage des mots de passe
    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    //@Bean // création d'utilisateurs en mémoire
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        PasswordEncoder passwordEncoder = passwordEncoder(); //encodage
        return  new InMemoryUserDetailsManager(
            User.withUsername("moh").password(passwordEncoder.encode("1234")).roles("USER").build(),
            User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build(),
            User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
        );
    }

    @Bean // création d'utilisateurs BD-v1
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    // création d'un filtre
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Route à prendre après authentification
        httpSecurity.formLogin().defaultSuccessUrl("/redact/index"); //formulaire de connexion par défaut et démarrage avec "/index"
        //httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/public/index").permitAll();//formulaire de connexion personnalisé et démarrage avec "/index"

        //ajout des autorisations // méthode 1
        httpSecurity.authorizeHttpRequests().requestMatchers("/redact/**").hasRole("REDACT");
        httpSecurity.authorizeHttpRequests().requestMatchers("/moder/**").hasRole("REDACT");
        httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests().requestMatchers("/public/**").permitAll();

        //Autoriser les différents outils de développement tels que 'bootstrap'
        httpSecurity.authorizeHttpRequests().requestMatchers("/css/**","/js/**","/webjars/**").permitAll();

        //requêtes à sécuriser
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated(); //toutes les requêtes doivent être authentifiées, sinon utiliser hasRole("Admin",...etc)
        httpSecurity.exceptionHandling().accessDeniedPage("/nonautorise"); // message si route non autorisée
        //httpSecurity.userDetailsService(userDetailService);//bd-v2
        httpSecurity.logout().logoutSuccessUrl("/index").permitAll();

        return httpSecurity.build();
    }
}
