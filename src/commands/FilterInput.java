package commands;

import java.util.ArrayList;
import java.util.List;

public class FilterInput {
    private String name;
    private String album;
    private ArrayList<String> tags = new ArrayList<>();
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;

    public FilterInput() {
    }

    /**
     *
     * @return numele AudioFile ului dupa care caut
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return albumul din care face parte
     */
    public String getAlbum() {
        return album;
    }

    /**
     *
     * @return lista de taguri dupa care sa caut
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @return Lyrics dupa care sa caut o melodie
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     *
     * @return genul muzicalal unei melodii
     */
    public String getGenre() {
        return genre;
    }

    /**
     *
     * @return intervalul de timp cand a fost lansata o melodie
     */
    public String getReleaseYear() {
        return releaseYear;
    }

    /**
     *
     * @return artistul unei melodii
     */
    public String getArtist() {
        return artist;
    }

    /**
     *
     * @return owner ul unui playlist sau podcast
     */
    public String getOwner() {
        return owner;
    }

}
