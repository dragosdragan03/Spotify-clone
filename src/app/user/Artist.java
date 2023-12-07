package app.user;

import app.audio.Files.Song;
import java.util.List;

public class Artist extends User {

    public Artist(String username, int age, String city) {
        super(username, age, city);
    }

    @Override
    public String addAlbum(String username, List<Song> songs) {

        return "Has added a new album successfully.";
    }
}
