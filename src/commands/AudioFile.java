package commands;

import fileio.input.PodcastInput;
import fileio.input.SongInput;

import java.util.ArrayList;

// fac o clasa pentru a mi putea retina intreaga structura a unui fisier audio
public class AudioFile {
    private SongInput song; // vreau sa retin melodia rezultata
    private PodcastInput podcast; // vreau sa retin podcastul rezultat
    private Playlist playlist;
    private String type; // pentru a vedea ce fel de fisier e

    public AudioFile() {
    }
    public AudioFile(SongInput song, String type) {
        this.song = song;
        this.type = type;
    }

    public AudioFile(PodcastInput podcast, String type) {
        this.podcast = podcast;
        this.type = type;
    }

    public AudioFile(Playlist playlist, String type) {
        this.playlist = playlist;
        this.type = type;
    }

    public SongInput getSong() {
        return song;
    }

    public PodcastInput getPodcast() {
        return podcast;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
