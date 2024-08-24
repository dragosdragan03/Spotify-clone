package commands.generals;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import fileio.input.LibraryInput;
import main.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GetTop5Playlists extends CommandExecute {

    private final ArrayList<String> result = new ArrayList<>();
    private final ArrayList<Playlist> publicPlaylists = new ArrayList<>();

    public GetTop5Playlists(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * fac o metoda pentru a adauga in arraylist ul nou creat, toate playlisturile, ca sa le  pot
     * sorta in functie de nr de followeri
     */
    private void addPlaylist() {
        // vreau sa retin toate playlisturile de tip public
        for (Playlist iter : getAllUsersPlaylists()) {
            if (iter.getTypePlaylist().equals("public")) {
                this.publicPlaylists.add(iter);
            }
        }
    }

    /**
     * suprascriu aceasta met pentru a putea gasi primele 5 playlisturi cu cei mai mutli followeri
     */
    @Override
    public void execute() {
        addPlaylist();
        // imi sorteaza descrescator vectorul meu de playlisturi dupa nr de followeri
        Collections.sort(this.publicPlaylists,
                Comparator.comparingInt(Playlist::getFollowers).reversed());
        final int maxSize = 5;
        for (int i = 0; i < this.publicPlaylists.size(); i++) {
            if (this.result.size() < maxSize) {
                this.result.add(this.publicPlaylists.get(i).getNamePlaylist());
            }
        }
    }

    /**
     * aceasta metoda este suprascrisa si are rolul de a
     * @return ArrayList ul cu primele 5 playlisturi cele mai urmarite
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (String iter : this.result) {
            output.getResult().add(iter);
        }
        return output;
    }
}
