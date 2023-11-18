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

    public FilterInput(){
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

//    @Override
//    public String toString() {
//        return "Filters{" +
//                "name='" + name + '\'' +
//                ", album='" + album + '\'' +
//                ", tags=" + tags +
//                ", lyrics='" + lyrics + '\'' +
//                ", genre='" + genre + '\'' +
//                ", releaseYear='" + releaseYear + '\'' +
//                ", artist='" + artist + '\'' +
//                ", owner='" + owner + '\'' +
//                '}';
//    }
}