package commands.playlist;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInputModified;
import main.Output;

import java.util.ArrayList;

public class ShowPlaylists extends CommandExecute {

    public class PlaylistShow { // vreau sa creez o clasa interna
        private String name = "";
        private ArrayList<String> songs = new ArrayList<>();
        private String visibility = "";
        private int followers = 0;

        public PlaylistShow(final String name, final ArrayList<String> songs,
                            final String visibility, final int followers) {
            this.name = name;
            this.songs = songs;
            this.visibility = visibility;
            this.followers = followers;
        }

        public final String getName() {
            return name;
        }

        public final ArrayList<String> getSongs() {
            return songs;
        }

        public final String getVisibility() {
            return visibility;
        }

        public final int getFollowers() {
            return followers;
        }
    }

    private ArrayList<PlaylistShow> result = new ArrayList<>();


    public ShowPlaylists(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * imi creeaza Arraylistul de PlaylistShow, pentru a putea afisa detaliile
     * tututor playlisturilor unui user
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getUserPlaylists().size() != 0) { // inseamna ca este ceva adaugat in lista
            for (Playlist iter : user.getUserPlaylists()) { // parcurg playlisturile userului meu
                String playlistName = iter.getNamePlaylist();
                ArrayList<String> songs = new ArrayList<>();
                // sa vad daca au fost adaugate pana acm in lista, melodii
                if (iter.getListSongs().size() != 0 && !iter.isShuffle()) {
                    for (SongInputModified song : iter.getListSongs()) {
                        songs.add(song.getSong().getName());
                    }
                } else {
                    for (SongInputModified song : iter.getCoppiedListSongs()) {
                        songs.add(song.getSong().getName());
                    }
                }

                String visibility = iter.getTypePlaylist();
                int followers = iter.getFollowers();
                PlaylistShow playlistShow =
                        new PlaylistShow(playlistName, songs, visibility, followers);
                this.result.add(playlistShow);
            }
        }
    }

    /**
     *  afiseaza detaliile tututor playlisturilor unui user
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (PlaylistShow iter : this.result) {
            output.getResult().add(iter);
        }
        return output;
    }
}
