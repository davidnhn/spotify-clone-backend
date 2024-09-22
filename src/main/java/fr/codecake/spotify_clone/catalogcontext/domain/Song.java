package fr.codecake.spotify_clone.catalogcontext.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "song")
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "songSequenceGenerator")
    @SequenceGenerator(name = "songSequenceGenerator", sequenceName = "song_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "title", nullable = false)
    private String title;


    @Column(name = "author", nullable = false)
    private String author;

    @Lob
    @Column(name = "cover", nullable = false)
    private byte[] cover;

    @Column(name = "cover_content_type", nullable = false)
    private String coverContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getCoverContentType() {
        return coverContentType;
    }

    public void setCoverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
    }
}


/*
 * Pourquoi utiliser à la fois une séquence et un UUID dans l'entité Song ?
 *
 * 1. Utilisation de la séquence pour la clé primaire (id) :
 * - Qu'est-ce qu'une séquence ?
 *   Une séquence est un générateur de valeurs uniques et croissantes souvent utilisées pour les clés primaires.
 * - Avantages :
 *   - Espace réduit : Les séquences occupent moins d'espace par rapport aux UUIDs.
 *   - Performance : Les index basés sur des séquences sont généralement plus petits et plus performants.
 *   - Prédictibilité : Les valeurs des séquences sont prévisibles et simples à utiliser.
 *   - Compatibilité : Les séquences sont natives à de nombreuses bases de données et bien supportées par Hibernate,
 *     qui peut utiliser des techniques comme le batching pour optimiser les insertions.
 *
 * 2. Utilisation de l'UUID pour l'identifiant public (publicId) :
 * - Qu'est-ce qu'un UUID ?
 *   Un UUID (Universally Unique Identifier) est un identifiant unique de 128 bits utilisé pour garantir l'unicité des identifiants.
 * - Avantages :
 *   - Unicité garantie : Parfait pour les systèmes distribués où il est crucial de garantir que chaque identifiant est unique sans conflit.
 *   - Sécurité : Les UUIDs sont difficiles à deviner et à itérer, ce qui empêche les utilisateurs malveillants de parcourir facilement toutes les entrées de la base de données.
 *
 * 3. Pourquoi combiner les deux ?
 * - Meilleur des deux mondes :
 *   - Clé primaire séquentielle (id) : Utilisée pour les performances de la base de données, les index plus petits, et les insertions optimisées.
 *   - UUID public (publicId) : Utilisé pour garantir l'unicité globale, pour des interactions sécurisées avec les systèmes externes et pour les besoins des systèmes distribués.
 *
 * 4. Résumé des avantages et des inconvénients :
 * - Séquence :
 *   - Avantages : Espace réduit, performances optimales des index, prédictibilité et simplicité.
 *   - Inconvénients : Non idéal pour les systèmes distribués, facile à deviner et à itérer.
 * - UUID :
 *   - Avantages : Unicité garantie dans les systèmes distribués, sécurité accrue contre les itérations malveillantes.
 *   - Inconvénients : Espace plus grand dans la base de données, index plus volumineux et potentiellement moins performants.
 *
 * En utilisant une séquence pour la clé primaire et un UUID pour l'identifiant public, nous combinons les avantages
 * de la performance et de l'efficacité des séquences avec la sécurité et l'unicité des UUIDs, optimisant ainsi les
 * opérations de la base de données tout en garantissant des identifiants sécurisés et uniques pour l'exposition externe.
 */
