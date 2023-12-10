package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class LikedContentPage implements Page {

// Liked Songs:\n\t[songname1 - songartist1, songname2 - songartist2, …]\n\nFollowed Playlists:\n\t[playlistname1 - owner1, playlistname2 - owner2, …]

    public String printCurrentPage(User user) {
        String currentPage;
        List<String> likedSong = new ArrayList<>();
        List<String> followedPlaylists = new ArrayList<>();
        for (Song iterSong : user.getLikedSongs()) {
            likedSong.add(iterSong.getName() + " - " + iterSong.getArtist());
        }

        for (Playlist iterPlaylist : user.getFollowedPlaylists()) {
            followedPlaylists.add(iterPlaylist.getName() + " - " + iterPlaylist.getOwner());
        }

        currentPage = "Liked songs:\n\t" + likedSong
                + "\n\nFollowed playlists:\n\t" + followedPlaylists;

        return currentPage;
    }
}
