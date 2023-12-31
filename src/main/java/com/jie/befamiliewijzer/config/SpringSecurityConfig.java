package com.jie.befamiliewijzer.config;
import com.jie.befamiliewijzer.filter.JwtRequestFilter;
import com.jie.befamiliewijzer.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }



    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        //JWT token authentication
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                                auth
                                        // Wanneer je deze uncomments, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
//                .requestMatchers("/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT,"/users/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/users/**").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.POST,"/persons").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT,"/persons/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/persons").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.GET,"/persons/nospouses").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/persons/solo").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/persons/multimedias/nomedia").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET,"/persons/**").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.DELETE,"/persons/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/relations").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/relations/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/relations/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/relations").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.GET, "/relations/**").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.POST,"/persons/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST,"/relations/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/eventtypes").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.GET, "/eventtypes/**").hasAnyRole("ADMIN","USER")

                                        .requestMatchers(HttpMethod.POST, "/events/*/multimedias").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.PUT, "/events/*/multimedias/*").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.POST, "/multimedias/*/media").hasAnyRole("ADMIN","USER")

                                        .requestMatchers(HttpMethod.POST, "/events/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/multimedias/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/events/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/events/**").hasAnyRole("ADMIN","USER")
                                        .requestMatchers(HttpMethod.DELETE, "/events/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/descendants/*").hasAnyRole("ADMIN","USER")

                                        .requestMatchers("/authenticated").authenticated()
                                        .requestMatchers("/authenticate").permitAll()/*alleen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/
                                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}