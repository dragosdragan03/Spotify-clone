package commands;

import fileio.input.PodcastInput;
import fileio.input.SongInputModified;

// fac o clasa pentru a mi putea retina intreaga structura a unui fisier audio
public class AudioFile {
    private SongInputModified songFile; // vreau sa retin melodia rezultata
    private PodcastInput podcastFile; // vreau sa retin podcastul rezultat
    private Playlist playlistFile;
    private int repeat;
    private String type; // pentru a vedea ce fel de fisier e

    /**
     * fac un constructor gol pentru a putea instantia clasa
     */
    public AudioFile() {
    }

    public AudioFile(final SongInputModified song, final String type) {
        this.songFile = song;
        this.type = type;
    }

    public AudioFile(final PodcastInput podcast, final String type) {
        this.podcastFile = podcast;
        this.type = type;
    }

    public AudioFile(final Playlist playlist, final String type) {
        this.playlistFile = playlist;
        this.type = type;
    }

    /**
     *
     * @return melodia pe care am incarcat 0
     */
    public SongInputModified getSongFile() {
        return songFile;
    }

    /**
     *
     * @return podcastul pe care l am incarcat
     */
    public PodcastInput getPodcastFile() {
        return podcastFile;
    }

    /**
     *
     * @return playlistul pe care l am incarcat
     */
    public Playlist getPlaylistFile() {
        return playlistFile;
    }

    /**
     *
     * @return daca este pe repeat sau nu AudioFile ul
     */
    public int getRepeat() {
        return repeat;
    }

    /**
     * setez acest camp in momentul in care execut metoda din Repeat
     * @param repeat
     */
    public void setRepeat(final int repeat) {
        this.repeat = repeat;
    }
}
