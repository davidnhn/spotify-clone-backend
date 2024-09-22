package fr.codecake.spotify_clone.usercontext.application;

import fr.codecake.spotify_clone.usercontext.ReadUserDTO;
import fr.codecake.spotify_clone.usercontext.domain.User;
import fr.codecake.spotify_clone.usercontext.mapper.UserMapper;
import fr.codecake.spotify_clone.usercontext.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public void syncWithIdp(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        User user = mapOauth2AttributesToUser(attributes);
        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            if (attributes.get("updated_at") != null) {
                Instant dbLastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
                Instant idpModifiedDate;
                if(attributes.get("updated_at") instanceof Instant) {
                    idpModifiedDate = (Instant) attributes.get("updated_at");
                } else {
                    idpModifiedDate = Instant.ofEpochSecond((Integer) attributes.get("updated_at"));
                }
                if(idpModifiedDate.isAfter(dbLastModifiedDate)) {
                    updateUser(user);
                }
            }
        } else {
            userRepository.saveAndFlush(user);
        }
    }

    private void updateUser(User user) {
        Optional<User> userToUpdateOptional = userRepository.findOneByEmail(user.getEmail());

        if(userToUpdateOptional.isPresent()) {
            User userToUpdateUser = userToUpdateOptional.get();
            userToUpdateUser.setFirstName(user.getFirstName());
            userToUpdateUser.setLastName(user.getLastName());
            userToUpdateUser.setEmail(user.getEmail());
            userToUpdateUser.setImageUrl(user.getImageUrl());
            userRepository.saveAndFlush(userToUpdateUser);
        }

        /*
        L'Optional n'est pas remplacé par userToUpdateUser.
        L'Optional est un conteneur temporaire pour la valeur renvoyée par la méthode findOneByEmail().
        Une fois que vous utilisez get() pour extraire la valeur de l'Optional,
        vous travaillez directement avec l'objet extrait (userToUpdateUser).
         */
    }


    public ReadUserDTO getAuthenticatedUserFormSecurityContext() {
        OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = mapOauth2AttributesToUser(principal.getAttributes());
        return userMapper.userToReadUserDTO(user);
    }



    /**
     * Mappe les attributs OAuth2 à une entité User.
     *
     * @param oauth2Attributes Map contenant les attributs OAuth2 de l'utilisateur.
     * @return Une instance de User avec les informations extraites des attributs OAuth2.

     * Étapes de la méthode :
     * 1. Crée une nouvelle instance de User.
     * 2. Extrait l'attribut "sub" (identifiant unique de l'utilisateur).
     * 3. Extrait et met en minuscules l'attribut "preferred_username" si disponible.
     * 4. Mappe le prénom à partir de "given_name" ou "name".
     * 5. Mappe le nom de famille à partir de "family_name".
     * 6. Mappe l'adresse email à partir de "email" ou dérive à partir de "sub" et "username".
     * 7. Mappe l'URL de l'image de profil à partir de "picture".
     * 8. Retourne l'objet User rempli avec les informations extraites.
     */
    private User mapOauth2AttributesToUser(Map<String, Object> oauth2Attributes) {
        User user = new User();
        String sub = String.valueOf(oauth2Attributes.get("sub"));

        String username = null;

        if(oauth2Attributes.get("preferred_username") != null) {
            username = ((String) oauth2Attributes.get("preferred_username")).toLowerCase();
        }

        if(oauth2Attributes.get("given_name") != null) {
            user.setFirstName((String) oauth2Attributes.get("given_name"));
        } else if (oauth2Attributes.get("name") != null) {
            user.setFirstName((String) oauth2Attributes.get("name"));
        }

        if(oauth2Attributes.get("family_name") != null) {
            user.setLastName((String) oauth2Attributes.get("family_name"));
        }

        if(oauth2Attributes.get("email") != null) {
            user.setEmail((String) oauth2Attributes.get("email"));
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            user.setEmail(username);
        }else {
            user.setEmail(sub);
        }

        if(oauth2Attributes.get("picture") != null) {
            user.setImageUrl((String) oauth2Attributes.get("picture"));
        }

        return user;
    }

    public Optional<ReadUserDTO> getByEmail(String email) {
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        return oneByEmail.map(userMapper::userToReadUserDTO);
    }

    public boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }
}

