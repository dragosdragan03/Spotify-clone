package commands.playlist;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import fileio.input.LibraryInput;
import main.Output;

public class CreatePlaylist extends CommandExecute {

    private String playlistName;
    private String message;
    public CreatePlaylist(Command command, LibraryInput library, String playlistName) {
        super(command, library);
        this.playlistName = playlistName;
    }

    private int verifyUserPlaylist() { // vreau sa verific daca exista playlist ul meu in lista
        if (getUserHistory().get(verifyUser(getUsername())).getUserPlaylists().size() != 0) { // daca exista ceva in lista
            for (Playlist iter : getUserHistory().get(verifyUser(getUsername())).getUserPlaylists())
                if (iter.getNamePlaylist().contains(this.playlistName))
                    return 1; // inseamna ca deja exista numele asta
        }
        return 0; // inseamnca ca nu exista
    }

    @Override
    public void execute() { // vreau sa creez un playlist pentru un user care prima oara este public
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul
            if (verifyUserPlaylist() == 0) {// inseamnca ca nu exista niciun playlist cu acest nume
                Playlist playlist = new Playlist(this.playlistName, "public", getUsername());
                getUserHistory().get(verifyUser(getUsername())).getUserPlaylists().add(playlist); // il pun in arraylist ul de playlisturi publice ale userului meu
                getAllUsersPlaylists().add(playlist); // il bag in lista cu playlisturi de pana acm
                this.message = "Playlist created successfully.";
                return;
            } else {
                this.message = "A playlist with the same name already exists.";
                return;
            }
        }
        this.message = "Nu exista userul pentru crearea playlistului";
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
