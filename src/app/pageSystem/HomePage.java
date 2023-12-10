package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class HomePage implements Page {

    public String printCurrentPage(User user) {
        String currentPage;
        List<String> likedSong = new ArrayList<>();
        List<String> followedPlaylists = new ArrayList<>();
        for (Song iterSong : user.getLikedSongs()) {
            likedSong.add(iterSong.getName());
        }

        for (Playlist iterPlaylist : user.getFollowedPlaylists()) {
            followedPlaylists.add(iterPlaylist.getName());
        }

        currentPage = "Liked songs:\n\t" + likedSong
                + "\n\nFollowed playlists:\n\t" + followedPlaylists;

        return currentPage;
    }
}
