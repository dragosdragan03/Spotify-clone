package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HomePage implements Page {

    /**
     * @param user
     * @return pagina unui user
     */
    public String printCurrentPage(final User user) {
        final int maxLimit = 5;
        String currentPage;

        List<Song> likedSongs = user.getLikedSongs();
        List<Song> sortedLikedSongs = new ArrayList<>(likedSongs);

        sortedLikedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());

        List<String> top5Songs = sortedLikedSongs.stream().limit(maxLimit)
                .map(LibraryEntry::getName)
                .toList();

        List<String> followedPlaylists = new ArrayList<>();

        for (Playlist iterPlaylist : user.getFollowedPlaylists()) {
            followedPlaylists.add(iterPlaylist.getName());
        }

        currentPage = "Liked songs:\n\t" + top5Songs + "\n\nFollowed playlists:\n\t"
                + followedPlaylists;

        return currentPage;
    }
}
