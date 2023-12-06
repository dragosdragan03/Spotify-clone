package app.audio.Files;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;

    public Song(String name, Integer duration, String album, ArrayList<String> tags, String lyrics,
                String genre, Integer releaseYear, String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }

    @Override
    public boolean matchesAlbum(String album) {
        return this.getAlbum().equalsIgnoreCase(album);
    }

    @Override
    public boolean matchesTags(ArrayList<String> tags) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tags) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean matchesLyrics(String lyrics) {
        return this.getLyrics().toLowerCase().contains(lyrics.toLowerCase());
    }

    @Override
    public boolean matchesGenre(String genre) {
        return this.getGenre().equalsIgnoreCase(genre);
    }

    @Override
    public boolean matchesArtist(String artist) {
        return this.getArtist().equalsIgnoreCase(artist);
    }

    @Override
    public boolean matchesReleaseYear(String releaseYear) {
        return filterByYear(this.getReleaseYear(), releaseYear);
    }

    private static boolean filterByYear(int year, String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }


    public void like() {
        likes++;
    }

    public void dislike() {
        likes--;
    }
}
