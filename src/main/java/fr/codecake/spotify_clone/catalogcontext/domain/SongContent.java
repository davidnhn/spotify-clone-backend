package fr.codecake.spotify_clone.catalogcontext.domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "song_content")
public class SongContent implements Serializable {

    @Id
    @Column(name = "song_id")
    private Long songId;

    @MapsId // Utilise la clé primaire de Song comme clé primaire de SongContent, établissant une relation un-à-un avec une clé primaire partagée.
    @OneToOne
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    private Song song;
    // @JoinColumn : Spécifie que la colonne song_id dans SongContent est une clé étrangère qui référence la colonne id dans Song.
    // referencedColumnName : Indique la colonne de l'entité principale (Song) référencée par la clé étrangère dans l'entité dépendante (SongContent).

    @Lob // Indique que le champ file est un grand objet binaire (BLOB binary large ocject), permettant de stocker de grandes quantités de données.
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}
