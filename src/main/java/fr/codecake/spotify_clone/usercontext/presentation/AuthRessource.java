package fr.codecake.spotify_clone.usercontext.presentation;

import fr.codecake.spotify_clone.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.codecake.spotify_clone.usercontext.ReadUserDTO;
import fr.codecake.spotify_clone.usercontext.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthRessource {

    private final UserService userService;

    private final ClientRegistration registration;

    public AuthRessource(UserService userService, ClientRegistrationRepository registrations) {
        this.userService = userService;
        this.registration = registrations.findByRegistrationId("okta");
    }

    @GetMapping("/get-authenticated-user")
    public ResponseEntity<ReadUserDTO> getAuthenticatedUser(@AuthenticationPrincipal OAuth2User user) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            userService.syncWithIdp(user);
            ReadUserDTO userFromAuthentication = userService.getAuthenticatedUserFormSecurityContext();
            return ResponseEntity.ok().body(userFromAuthentication);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String issuerUri = registration.getProviderDetails().getIssuerUri();
        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        Object[] params = {issuerUri, registration.getClientId(), originUrl};
        String logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
        request.getSession().invalidate();
        return ResponseEntity.ok().body(Map.of("logoutUrl", logoutUrl));
    }
}

/*
 * Authentification et Synchronisation :

 * 1. L'utilisateur s'authentifie via OAuth2.
 * 2. La méthode getAuthenticatedUser est appelée pour obtenir les détails de l'utilisateur.
 * 3. La méthode getAuthenticatedUser appelle syncWithIdp pour synchroniser les informations utilisateur avec le fournisseur d'identité.
 * 4. La méthode syncWithIdp utilise mapOauth2AttributesToUser pour convertir les attributs OAuth2 en entité User.
 * 5. Si l'utilisateur existe déjà, syncWithIdp appelle updateUser pour mettre à jour les informations si nécessaire.

 * Déconnexion :

 * 1. La méthode logout est appelée pour déconnecter l'utilisateur.
 * 2. Construit une URL de déconnexion basée sur les informations du fournisseur d'identité.
 * 3. Invalide la session utilisateur.
 */


/** Logout
 * AuthRessource gère les requêtes d'authentification et de déconnexion des utilisateurs.
 *
 * @param userService Service pour gérer la logique métier des utilisateurs.
 * @param registrations Repository pour gérer les enregistrements des clients OAuth2.
 *
 * ClientRegistrationRepository :
 * - Interface fournie par Spring Security OAuth2 Client pour gérer les enregistrements des clients OAuth2.
 * - Utilisé pour obtenir les informations d'enregistrement des clients, telles que les détails du fournisseur d'identité,
 *   les informations d'identification du client (ID client et secret client), et d'autres configurations nécessaires
 *   pour interagir avec le fournisseur d'identité.
 *
 * ClientRegistration :
 * - Représente les informations de configuration nécessaires pour s'authentifier auprès d'un fournisseur OAuth2.
 * - Contient des informations telles que l'ID client, le secret client, l'URI du fournisseur d'identité, les scopes,
 *   et d'autres paramètres de configuration.
 * - Utilisé pour configurer et initialiser les interactions avec un fournisseur OAuth2, y compris les flux
 *   d'authentification et d'autorisation.
 *
 * Utilisation dans la méthode logout :
 *
 * 1. Obtenir l'URI de l'issuer :
 *    - registration.getProviderDetails().getIssuerUri() : Récupère l'URI du fournisseur d'identité (issuer).
 *
 * 2. Obtenir l'URL d'origine :
 *    - request.getHeader(HttpHeaders.ORIGIN) : Récupère l'en-tête HTTP `Origin` de la requête, qui contient l'URL de base de l'application cliente.
 *
 * 3. Construire l'URL de déconnexion :
 *    - Utilise MessageFormat.format pour construire une URL de déconnexion avec les paramètres `issuerUri`, `clientId`, et `originUrl`.
 *    - Exemple d'URL de déconnexion : `https://{issuerUri}/v2/logout?client_id={clientId}&returnTo={originUrl}`.
 *
 * 4. Invalider la session :
 *    - request.getSession().invalidate() : Invalide la session actuelle de l'utilisateur, ce qui déconnecte l'utilisateur localement.
 *
 * 5. Retourner l'URL de déconnexion :
 *    - Retourne une réponse HTTP 200 OK avec un corps JSON contenant l'URL de déconnexion.
 */


/**
 * Déconnexion de l'utilisateur.
 *
 * String issuerUri = "https://dev-123456.okta.com/";
 * String clientId = "0oa1abcd1234efgh5678";
 * String originUrl = "http://localhost:8080";
 * Object[] params = {issuerUri, clientId, originUrl};
 * String logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
 *
 * 1. Obtenir l'URI de l'issuer :
 *    - registration.getProviderDetails().getIssuerUri() : Récupère l'URI du fournisseur d'identité (issuer).
 *
 * 2. Obtenir l'URL d'origine :
 *    - request.getHeader(HttpHeaders.ORIGIN) : Récupère l'en-tête HTTP `Origin` de la requête, qui contient l'URL de base de l'application cliente.
 *
 * 3. Construire l'URL de déconnexion :
 *    - Utilise MessageFormat.format pour construire une URL de déconnexion avec les paramètres `issuerUri`, `clientId`, et `originUrl`.
 *    - Exemple d'URL de déconnexion : `https://dev-123456.okta.com/v2/logout?client_id=0oa1abcd1234efgh5678&returnTo=http://localhost:8080`.
 *
 * 4. Invalider la session :
 *    - request.getSession().invalidate() : Invalide la session actuelle de l'utilisateur, ce qui déconnecte l'utilisateur localement.
 *
 * 5. Retourner l'URL de déconnexion :
 *    - Retourne une réponse HTTP 200 OK avec un corps JSON contenant l'URL de déconnexion.
 */
