package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Artist extends User {

    private List<Album> albums; // fac o lista de albume ale unui user

    public Artist(String username, int age, String city) {
        super(username, age, city);
        albums = new ArrayList<>();
    }

    @Override
    public List<Album> getAlbumsOfAnArtist() {
        return albums;
    }

    private Song copySongInputToSong(final SongInput songInput) {
        Song song = new Song(songInput.getName(),
                songInput.getDuration(),
                songInput.getAlbum(),
                songInput.getTags(),
                songInput.getLyrics(),
                songInput.getGenre(),
                songInput.getReleaseYear(),
                songInput.getArtist());
        return song;
    }

    private int isDuplicated(final List<SongInput> songs) {
        Set<String> uniqueItems = new HashSet<>();
        for (SongInput iterSongInput : songs) {
            if (!uniqueItems.add(iterSongInput.getName())) {
                return 1; // inseamna ca este duplicat
            }
        }
        return 0; // inseamna ca nu este duplicat
    }

    @Override
    public String addAlbum(final List<SongInput> songs, final String name, final int releaseYear, final String description) {

        for (Album iter : albums) {
            if (iter.getName().equals(name)) { // sa vad
                return getUsername() + " has another album with the same name.";
            }
        }

        if (isDuplicated(songs) == 1) {
            return getUsername() + " has the same song at least twice in this album.";
        }

        Album newAlbum = new Album(name, getUsername(), releaseYear, description);
        for (SongInput iterSong : songs) {
            Song song = copySongInputToSong(iterSong);
            if (!Admin.getSongs().contains(song)) {
                Admin.getSongs().add(song);
            }
            newAlbum.getSongs().add(song);
        }
        albums.add(newAlbum);

        return getUsername() + " has added new album successfully.";
    }
}
