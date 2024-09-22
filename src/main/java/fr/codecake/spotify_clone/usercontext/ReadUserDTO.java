package fr.codecake.spotify_clone.usercontext;

public record ReadUserDTO(
        String firstName,
        String lastName,
        String email,
        String imageUrl
) {
}
