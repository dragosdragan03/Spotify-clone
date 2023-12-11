package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class Album extends AudioCollection {

    @Getter
    private int releaseYear;
    @Getter
    private String description;
    @Getter
    private final List<Song> songs = new ArrayList<>();
//    @Getter
//    private final int numberLikesAlbum = 0;

    public Album(final String name, final String owner, final int releaseYear, final String description) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
    }

    public void addSong(final Song song) {
        songs.add(song);
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    public int getNumberLikesAlbum() {
        int sum = 0;
        for (Song iterSong : songs) {
            sum += iterSong.getLikes();
        }
        return sum;
    }

}
