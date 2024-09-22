package fr.codecake.spotify_clone.sharedkernel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity<T> implements Serializable {

    public abstract T getId();

    @CreatedDate
    @Column(name = "created_date",updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}


/*
 * Instant:
 * Description : Représente un point précis sur la ligne de temps (le nombre de secondes écoulées depuis l'époque UNIX, c'est-à-dire le 1er janvier 1970 à 00:00:00 UTC).
 * Précision : Jusqu'à la nanoseconde.
 * Zone Horaire : Toujours en UTC, ce qui signifie qu'il est indépendant de la zone horaire locale.
 * Usage : Idéal pour des horodatages absolus ou pour des enregistrements en base de données où la cohérence et l'universalité des horodatages sont critiques.
 *
 * LocalDateTime:
 * Description : Représente une date et une heure sans fuseau horaire. C'est une combinaison de LocalDate et LocalTime.
 * Précision : Jusqu'à la nanoseconde.
 * Zone Horaire : Indépendant de toute zone horaire. Il représente une date et une heure tels qu'ils apparaissent dans un calendrier local.
 * Usage : Idéal pour représenter des dates et heures locaux, mais moins approprié pour des horodatages universels en base de données.
 *
 * Comparaison et Choix pour l'Auditing:
 * Universalité :
 * - Instant : Toujours en UTC, ce qui en fait un choix universel et standardisé pour les horodatages. Il évite les ambiguïtés liées aux fuseaux horaires.
 * - LocalDateTime : Dépendant du contexte local sans information de fuseau horaire, ce qui peut introduire des ambiguïtés lorsqu'on travaille avec des systèmes distribués ou des bases de données.
 *
 * Conversion :
 * - Instant : Peut être facilement converti en d'autres types de temps (comme LocalDateTime) en ajoutant des informations de fuseau horaire.
 * - LocalDateTime : Peut être converti en Instant mais nécessite des informations de fuseau horaire pour être précis.
 *
 * Précision et Cohérence :
 * - Instant : Utilise UTC donc les horodatages sont cohérents partout.
 * - LocalDateTime : Peut varier selon les réglages locaux du système, ce qui peut causer des incohérences.
 */


/*
 * Pourquoi utiliser Serializable ?
 * L'interface Serializable est utilisée pour permettre la sérialisation d'objets,
 * c'est-à-dire la conversion d'un objet en une forme qui peut être facilement stockée ou transmise.
 * Voici quelques raisons courantes pour lesquelles Serializable est utilisé avec des entités JPA :
 *
 * Transmission des données :
 * - RMI, JMS, ou autres frameworks de communication : Les objets doivent être sérialisables pour être transmis sur le réseau.
 *
 * Stockage des états de session :
 * - Applications web : Les objets de session doivent être sérialisables pour permettre la réplication de sessions dans des environnements distribués.
 *
 * Caches distribués :
 * - Caches comme Ehcache, Hazelcast, ou Infinispan : Pour stocker des entités dans un cache distribué, elles doivent souvent être sérialisables.
 *
 * Sauvegarde et restauration :
 * - Sauvegarde des objets : Les objets peuvent être sérialisés pour la sauvegarde et restaurés plus tard.
 */
