package commands;

import fileio.input.PodcastInput;
import fileio.input.SongInputModified;

// fac o clasa pentru a mi putea retina intreaga structura a unui fisier audio
public class AudioFile {
    private SongInputModified songFile; // vreau sa retin melodia rezultata
    private PodcastInput podcastFile; // vreau sa retin podcastul rezultat
    private Playlist playlistFile;
    private String type; // pentru a vedea ce fel de fisier e

    public AudioFile() {
    }
    public AudioFile(SongInputModified song, String type) {
        this.songFile = song;
        this.type = type;
    }

    public AudioFile(PodcastInput podcast, String type) {
        this.podcastFile = podcast;
        this.type = type;
    }

    public AudioFile(Playlist playlist, String type) {
        this.playlistFile = playlist;
        this.type = type;
    }

    public SongInputModified getSongFile() {
        return songFile;
    }

    public PodcastInput getPodcastFile() {
        return podcastFile;
    }

    public Playlist getPlaylistFile() {
        return playlistFile;
    }
}
