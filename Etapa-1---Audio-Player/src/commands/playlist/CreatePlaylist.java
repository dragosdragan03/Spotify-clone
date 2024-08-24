package commands.playlist;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class CreatePlaylist extends CommandExecute {

    private String playlistName;
    private String message;
    public CreatePlaylist(final Command command, final LibraryInput library,
                          final String playlistName) {
        super(command, library);
        this.playlistName = playlistName;
    }

    private int verifyUserPlaylist() { // vreau sa verific daca exista playlist ul meu in lista
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getUserPlaylists().size() != 0) {
            for (Playlist iter : user.getUserPlaylists()) {
                if (iter.getNamePlaylist().contains(this.playlistName)) {
                    return 1; // inseamna ca deja exista numele asta
                }
            }
        }
        return 0; // inseamnca ca nu exista
    }

    /**
     * creez un playlist si l adaug in lista de playlisturi a userului meu +
     * in lista globala de playlisturi create de toti userii
     */
    @Override
    public void execute() {
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul
            if (verifyUserPlaylist() == 0) { // inseamnca ca nu exista niciun playlist cu acest nume
                Playlist playlist = new Playlist(this.playlistName, "public", getUsername());
                // il pun in arraylist ul de playlisturi publice ale userului meu
                getUserHistory().get(verifyUser(getUsername())).getUserPlaylists().add(playlist);
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

    /**
     *
     * @return mesajul creat in metoda execute in functie daca a fost sau nu creat playlistul
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
