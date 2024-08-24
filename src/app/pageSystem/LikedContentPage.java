package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class LikedContentPage implements Page {

    /**
     *
     * @param user cel caruia i se va afisa pagina
     * @return pagina unui user cu toate melodiile apreciate si playlisturule la care a dat follow
     */
    public String printCurrentPage(final User user) {
        String currentPage;
        List<String> likedSong = new ArrayList<>();
        for (Song iterSong : user.getLikedSongs()) {
            likedSong.add(iterSong.getName() + " - " + iterSong.getArtist());
        }

        List<String> followedPlaylists = new ArrayList<>();

        for (Playlist iterPlaylist : user.getFollowedPlaylists()) {
            followedPlaylists.add(iterPlaylist.getName() + " - " + iterPlaylist.getOwner());
        }

        currentPage = "Liked songs:\n\t" + likedSong
                + "\n\nFollowed playlists:\n\t" + followedPlaylists;

        return currentPage;
    }
}
