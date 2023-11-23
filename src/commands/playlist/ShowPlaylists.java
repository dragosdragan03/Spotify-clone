package commands.playlist;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import fileio.input.SongInputModified;
import main.Output;

import java.util.ArrayList;

public class ShowPlaylists extends CommandExecute {

    public class PlaylistShow { // vreau sa creez o clasa interna
        private String name = "";
        private ArrayList<String> songs = new ArrayList<>();
        private String visibility = "";
        private int followers = 0;

        public PlaylistShow(String name, ArrayList<String> songs, String visibility, int followers) {
            this.name = name;
            this.songs = songs;
            this.visibility = visibility;
            this.followers = followers;
        }

        public String getName() {
            return name;
        }

        public ArrayList<String> getSongs() {
            return songs;
        }

        public String getVisibility() {
            return visibility;
        }

        public int getFollowers() {
            return followers;
        }
    }

    private ArrayList<PlaylistShow> result = new ArrayList<>();


    public ShowPlaylists(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getUserPlaylists().size() != 0) {// inseamna ca este ceva adaugat in lista
            for (Playlist iter : user.getUserPlaylists()) { // fac un for ca sa parcurg playlisturile userului meu
                String playlistName = iter.getNamePlaylist();
                ArrayList<String> songs = new ArrayList<>();
                if (iter.getListSongs().size() != 0) {// sa vad daca au fost bagate pana acm in lista melodii
                    if (iter.isShuffle() == false) {
                        for (SongInputModified song : iter.getListSongs()) {
                            songs.add(song.getSong().getName());
                        }
                    } else {
                        for (SongInputModified song : iter.getCoppiedListSongs()) {
                            songs.add(song.getSong().getName());
                        }
                    }
                }
                String visibility = iter.getTypePlaylist();
                int followers = iter.getFollowers();
                PlaylistShow playlistShow = new PlaylistShow(playlistName, songs, visibility, followers);
                this.result.add(playlistShow);
            }
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (PlaylistShow iter : this.result)
            output.getResult().add(iter);
        return output;
    }
}
