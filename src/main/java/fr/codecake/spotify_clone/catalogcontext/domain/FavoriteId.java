package fr.codecake.spotify_clone.catalogcontext.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class FavoriteId implements Serializable {

    UUID songPublicId;

    String userEmail;

    // Constructeur vide nécessaire pour JPA
    public FavoriteId() {
    }

    public FavoriteId(UUID songPublicId, String userEmail) {
        this.songPublicId = songPublicId;
        this.userEmail = userEmail;
    }

    public UUID getSongPublicId() {
        return songPublicId;
    }

    public void setSongPublicId(UUID songPublicId) {
        this.songPublicId = songPublicId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    // Méthode equals pour comparer les instances de FavoriteId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(songPublicId, that.songPublicId) && Objects.equals(userEmail, that.userEmail);
    }

    // Méthode hashCode pour générer un code de hachage pour FavoriteId
    @Override
    public int hashCode() {
        return Objects.hash(songPublicId, userEmail);
    }
}

/*
 * L'annotation @IdClass est utilisée pour spécifier une clé primaire composite dans une entité JPA.
 *
 * La classe FavoriteId encapsule les champs songPublicId et userEmail pour représenter la clé primaire composite.
 *
 * Pourquoi utiliser FavoriteId ?
 * - JPA nécessite une classe distincte pour représenter une clé primaire composite.
 * - La classe FavoriteId implémente Serializable et définit les méthodes equals et hashCode.
 *
 * Méthode equals :
 * - Utilisée pour comparer deux instances de FavoriteId.
 * - Deux instances sont égales si leurs champs songPublicId et userEmail sont égaux.
 *
 * Méthode hashCode :
 * - Génère un code de hachage pour l'instance de FavoriteId.
 * - Utilisé dans les collections basées sur des hachages (par exemple, HashMap, HashSet).
 *
 * Constructeur vide :
 * - Nécessaire pour JPA pour créer des instances par réflexion lors du chargement des entités depuis la base de données.
 *
 * Constructeur complet :
 * - Pratique pour créer des instances de FavoriteId avec des valeurs spécifiques.
 * - Facilite la création d'objets lors de l'initialisation ou des tests.
 */


/*
 * Utilisation de @IdClass pour une clé primaire composite :
 *
 * La réflexion :
 * - La réflexion en Java permet d'inspecter et de manipuler les propriétés des classes et des objets à l'exécution.
 * - JPA utilise la réflexion pour créer des instances de classes d'entités, accéder aux champs et méthodes, et gérer les relations entre les entités.
 * - Le constructeur vide dans la classe FavoriteId est nécessaire pour que JPA puisse créer des instances de la classe FavoriteId par réflexion.
 *
 * Méthodes hashCode et equals :
 * - Utilisées dans les collections basées sur des hachages (par exemple, HashMap, HashSet).
 * - hashCode : Retourne un code de hachage pour l'objet. Utilisé pour déterminer le compartiment de stockage dans une table de hachage.
 * - equals : Détermine si deux objets sont égaux. Utilisé pour vérifier l'égalité des clés dans une HashMap ou l'unicité des éléments dans une HashSet.
 *
 * Exemple :
 * - HashMap utilise hashCode pour déterminer où stocker une entrée et equals pour gérer les collisions et vérifier l'égalité des clés.
 * - HashSet utilise hashCode pour déterminer le compartiment de stockage et equals pour vérifier l'unicité des éléments.
 *
 * FavoriteId :
 * - La classe FavoriteId encapsule les champs songPublicId et userEmail pour représenter une clé primaire composite.
 * - hashCode et equals sont implémentés pour garantir que les instances de FavoriteId peuvent être utilisées efficacement dans les collections basées sur des hachages.
 * - Le constructeur vide est nécessaire pour que JPA puisse créer des instances de FavoriteId par réflexion.
 */
