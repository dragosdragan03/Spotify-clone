package app.searchBar;

import app.audio.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {

    public static List<LibraryEntry> filterByName(List<LibraryEntry> entries, String name) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesName(name)) {
                result.add(entry);
            }
        }
        return result;
    }

    public static List<LibraryEntry> filterByAlbum(List<LibraryEntry> entries, String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }

    public static List<LibraryEntry> filterByTags(List<LibraryEntry> entries, ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }

    public static List<LibraryEntry> filterByLyrics(List<LibraryEntry> entries, String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }

    public static List<LibraryEntry> filterByGenre(List<LibraryEntry> entries, String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }

    public static List<LibraryEntry> filterByArtist(List<LibraryEntry> entries, String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }

    public static List<LibraryEntry> filterByReleaseYear(List<LibraryEntry> entries, String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }

    public static List<LibraryEntry> filterByOwner(List<LibraryEntry> entries, String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }

    public static List<LibraryEntry> filterByPlaylistVisibility(List<LibraryEntry> entries, String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }

    public static List<LibraryEntry> filterByFollowers(List<LibraryEntry> entries, String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    private static List<LibraryEntry> filter(List<LibraryEntry> entries, FilterCriteria criteria) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (criteria.matches(entry)) {
                result.add(entry);
            }
        }
        return result;
    }

    @FunctionalInterface
    private interface FilterCriteria {
        boolean matches(LibraryEntry entry);
    }
}
