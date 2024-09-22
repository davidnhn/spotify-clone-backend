package fr.codecake.spotify_clone.catalogcontext.application.vo;

import jakarta.validation.constraints.NotBlank;

public record SongTitleVO(
        @NotBlank String value
) {
}


/*
 * Value Objects (VO) et Data Transfer Objects (DTO) :
 *
 * Value Objects (VO) :
 * - Les VO encapsulent des concepts de domaine spécifiques sans identité propre.
 * - Ils sont généralement immuables, ce qui signifie que leur état ne change pas après leur création.
 * - L'égalité des VO est basée sur les valeurs qu'ils contiennent.
 * - Utilisation : Les VO sont utilisés pour représenter des valeurs immuables et valides, telles que des titres de chansons, des auteurs, des adresses, etc.
 *
 * Exemple de VO :
 * public record SongTitleVO(@NotBlank String value) {}
 * public record SongAuthorVO(@NotBlank String value) {}
 *
 * Data Transfer Objects (DTO) :
 * - Les DTO sont utilisés pour transférer des données entre différentes couches d'une application.
 * - Ils sont souvent de simples objets avec des champs publics ou des getters et setters.
 * - Les DTO sont généralement mutables et servent à transporter des VO et d'autres données.
 * - Utilisation : Les DTO sont utilisés pour transférer des données entre les couches de service, de présentation et de persistance.
 *
 * Exemple de DTO :
 * public record SongDTO(Long id, SongTitleVO title, SongAuthorVO author, String cover, String coverContentType) {}
 *
 * Différences :
 * - VO : Encapsulent des valeurs immuables et des concepts de domaine sans identité propre. Utilisés pour garantir la validité des données.
 * - DTO : Utilisés pour transférer des données entre différentes couches de l'application. Peuvent inclure des VO et d'autres champs.
 *
 * Utilisation conjointe :
 * - Les VO sont utilisés dans les entités et les DTO pour garantir que les valeurs respectent certaines contraintes et sont immuables.
 * - Les DTO transportent les VO (et d'autres données) entre les différentes couches de l'application, facilitant ainsi la communication et le transfert de données.
 *
 * Exemple de Mapper :
 * @Mapper(componentModel = "spring")
 * public interface SongMapper {
 *     SongDTO songToSongDTO(Song song);
 * }
 *
 * En utilisant les VO et les DTO ensemble, on assure une meilleure structuration des données, une validation cohérente, et une encapsulation des concepts de domaine,
 * tout en facilitant le transfert de données entre les différentes couches de l'application.
 */
