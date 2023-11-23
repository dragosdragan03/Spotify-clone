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

    public GetTop5Playlists(Command command, LibraryInput library) {
        super(command, library);
    }

    /**
     * fac o metoda pentru a adauga in arraylist ul nou creat toate playlisturi pentru ca mai departe sa le sortez in functie de
     */
    private void addPlaylist() {
        for (Playlist iter : getAllUsersPlaylists()) { // vreau sa retin toate playlisturile de tip public
            if (iter.getTypePlaylist().equals("public"))
                this.publicPlaylists.add(iter);
        }
    }

//    private int indexPlaylist(String namePlaylist) {
//        for (int i = 0; i < this.publicPlaylists.size(); i++)
//            if (this.publicPlaylists.get(i).getNamePlaylist().equals(namePlaylist))
//                return i;
//        return -1;
//    }

    @Override
    public void execute() {
        addPlaylist();
        // trebuie sa ordonez crescator vectorul meu dupa nr de followeri
        Collections.sort(this.publicPlaylists, Comparator.comparingInt(Playlist::getFollowers).reversed()); // imi sorteaza descrescator vectorul meu de playlisturi dupa nr de followeri
        for (int i = 0; i < this.publicPlaylists.size(); i++) {
            if (this.result.size() < 5)
                this.result.add(this.publicPlaylists.get(i).getNamePlaylist());
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (String iter : this.result)
            output.getResult().add(iter);
        return output;
    }
}
