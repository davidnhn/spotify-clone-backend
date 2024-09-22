package fr.codecake.spotify_clone.infrastucture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "api/songs").permitAll()
                .requestMatchers(HttpMethod.GET, "api/songs/search").permitAll()
                .anyRequest().authenticated())
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }

}

/*
 * Configuration de la sécurité Spring avec CSRF et OAuth2 :
 *
 * 1. Configuration CSRF :
 * - Utilisation de CookieCsrfTokenRepository pour stocker le token CSRF dans un cookie.
 * - .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) :
 *   Stocke le token CSRF dans un cookie accessible côté client.
 * - .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()) :
 *   Gère l'extraction et la validation des tokens CSRF dans les requêtes.
 *
 * 2. Configuration OAuth2 :
 * - .oauth2Login(Customizer.withDefaults()) :
 *   Active OAuth2 pour l'authentification utilisateur avec des paramètres par défaut.
 * - .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) :
 *   Configure le serveur de ressources OAuth2 pour utiliser JWT pour la validation des tokens d'accès.
 * - .oauth2Client(Customizer.withDefaults()) :
 *   Permet à l'application de fonctionner comme un client OAuth2 pour accéder à des ressources protégées.
 */
