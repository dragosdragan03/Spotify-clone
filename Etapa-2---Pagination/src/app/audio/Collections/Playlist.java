package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

/**
 * The type Playlist.
 */
@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    private Integer followers;
    private int timestamp;

    /**
     * Instantiates a new Playlist.
     *
     * @param name  the name
     * @param owner the owner
     */
    public Playlist(final String name, final String owner) {
        this(name, owner, 0);
    }

    /**
     * Instantiates a new Playlist.
     *
     * @param name      the name
     * @param owner     the owner
     * @param timestamp the timestamp
     */
    public Playlist(final String name, final String owner, final int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }

    /**
     * Contains song boolean.
     *
     * @param song the song
     * @return the boolean
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * Add song.
     *
     * @param song the song
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Remove song.
     *
     * @param song the song
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * Remove song.
     *
     * @param index the index
     */
    public void removeSong(final int index) {
        songs.remove(index);
    }

    /**
     * Switch visibility.
     */
    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    /**
     * Increase followers.
     */
    public void increaseFollowers() {
        followers++;
    }

    /**
     * Decrease followers.
     */
    public void decreaseFollowers() {
        followers--;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean isVisibleToUser(final String user) {
        return this.getVisibility() == Enums.Visibility.PUBLIC
               || (this.getVisibility() == Enums.Visibility.PRIVATE
                   && this.getOwner().equals(user));
    }

    @Override
    public boolean matchesFollowers(final String followerNum) {
        return filterByFollowersCount(this.getFollowers(), followerNum);
    }

    private static boolean filterByFollowersCount(final int count, final String query) {
        if (query.startsWith("<")) {
            return count < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return count > Integer.parseInt(query.substring(1));
        } else {
            return count == Integer.parseInt(query);
        }
    }
}
