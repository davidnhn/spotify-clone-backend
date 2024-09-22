package fr.codecake.spotify_clone.infrastucture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"fr.codecake.spotify_clone.catalogcontext.repository", "fr.codecake.spotify_clone.usercontext.repository"})
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {
}


/*
 * Explication de l'Annotation @Repository et la Configuration de Spring Data JPA :
 *
 * L'annotation @Repository :
 * - Indique que la classe est un composant Spring interagissant avec la base de données.
 * - Pour les interfaces qui étendent JpaRepository, @Repository n'est pas nécessaire car Spring Data JPA les gère automatiquement.
 *
 * Configuration centralisée avec @EnableJpaRepositories :
 * - @EnableJpaRepositories : Active la création automatique des beans de repository pour les interfaces dans les packages spécifiés.
 * - @EnableTransactionManagement : Active la gestion des transactions dans Spring.
 * - @EnableJpaAuditing : Active l'audit des entités JPA, permettant l'utilisation des annotations telles que @CreatedDate et @LastModifiedDate.
 *
 * Exemple de Configuration centralisée :
 * @Configuration
 * @EnableJpaRepositories({"fr.codecake.spotify_clone.catalogcontext.repository", "fr.codecake.spotify_clone.usercontext.repository"})
 * @EnableTransactionManagement
 * @EnableJpaAuditing
 * public class DatabaseConfiguration {
 * }
 *
 * En utilisant @EnableJpaRepositories dans une classe de configuration, Spring gère automatiquement les repositories dans les packages spécifiés,
 * rendant l'annotation @Repository redondante pour les interfaces qui étendent JpaRepository.
 */
