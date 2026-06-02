    package com.example.SmartCity.config;

    import com.example.SmartCity.security.JwtAuthenticationFilter;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import java.util.List;
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(prePostEnabled = true)
    public class SecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://smartcity-ochre.vercel.app"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source =
                    new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authorizeHttpRequests(auth -> auth

                            // 🔓 AUTH APIs
                            .requestMatchers("/api/auth/**").permitAll()

                            // 🔓 PUBLIC READ (Decision Support)
                            .requestMatchers(HttpMethod.GET,
                                    "/api/colleges",
                                    "/api/colleges/**"
                            ).permitAll()

                            .requestMatchers(HttpMethod.GET, "/api/hospitals/nearest").permitAll()

                            // 🔒 ADMIN WRITE (College Management)
                            .requestMatchers(HttpMethod.POST, "/api/colleges").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/colleges/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/colleges/**").hasRole("ADMIN")

                            // 🔒 OTHER MODULES (Still Protected)
                            .requestMatchers("/api/hospitals/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/jobs/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/dashboard/**").hasAnyRole("USER", "ADMIN")

                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter(),
                            UsernamePasswordAuthenticationFilter.class
                    );

            return http.build();
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter();
        }
    }