package fileio.input;

import java.util.ArrayList;

public final class LibraryInput {
    private ArrayList<SongInput> songs;
    private ArrayList<PodcastInput> podcasts;
    private ArrayList<UserInput> users;

    public LibraryInput() {
    }

    public ArrayList<SongInput> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public ArrayList<PodcastInput> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(ArrayList<PodcastInput> podcasts) {
        this.podcasts = podcasts;
    }

    public ArrayList<UserInput> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserInput> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "LibraryInput{" +
                "songs=" + songs +
                ", podcasts=" + podcasts +
                ", users=" + users +
                '}';
    }
}
