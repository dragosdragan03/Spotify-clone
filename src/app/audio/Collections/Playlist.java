package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    private Integer followers;
    private int timestamp;

    public Playlist(String name, String owner) {
        this(name, owner, 0);
    }

    public Playlist(String name, String owner, int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }

    public boolean containsSong(Song song) {
        return songs.contains(song);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }
    public void removeSong(int index) {
        songs.remove(index);
    }

    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    public void increaseFollowers() {
        followers++;
    }

    public void decreaseFollowers() {
        followers--;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }

    @Override
    public boolean isVisibleToUser(String user) {
        return this.getVisibility() == Enums.Visibility.PUBLIC ||
                (this.getVisibility() == Enums.Visibility.PRIVATE && this.getOwner().equals(user));
    }

    @Override
    public boolean matchesFollowers(String followers) {
        return filterByFollowersCount(this.getFollowers(), followers);
    }

    private static boolean filterByFollowersCount(int count, String query) {
        if (query.startsWith("<")) {
            return count < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return count > Integer.parseInt(query.substring(1));
        } else {
            return count == Integer.parseInt(query);
        }
    }
}
