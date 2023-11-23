package commands.playlist;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class SwitchVisibility extends CommandExecute {

    private int playlistId;
    private String message;

    public SwitchVisibility(Command command, LibraryInput library, int playlistId) {
        super(command, library);
        this.playlistId = playlistId;
    }

    // fac o metoda pentru a mi returna playlistul userului din toata lista de playlisturi
    private int playlistPosition(Playlist playlist) {
        for (int i = 0; i <= getAllUsersPlaylists().size(); i++) // fac un for pentru a parcurge toata lista
            if (getAllUsersPlaylists().get(i) == playlist)
                return i; // returnez pozitia din vector pentru a l putea seta

        return -1;
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername())); // retin userul sa vad pe ce user fac modificari
        if (user.getUserPlaylists() != null) {// sa vad daca am mai incarcat playlisturi in el
            if (user.getUserPlaylists().size() >= this.playlistId) {
                int pozitiePlaylist = playlistPosition(user.getUserPlaylists().get(this.playlistId - 1));
                if (user.getUserPlaylists().get(this.playlistId - 1).getTypePlaylist().equals("public")) {// inseamna ca este un playlist public si vreau sa l fac privat
                    user.getUserPlaylists().get(this.playlistId - 1).setTypePlaylist("private"); // deoarece este indexat de la 0
                    getAllUsersPlaylists().get(pozitiePlaylist).setTypePlaylist("private");
                    this.message = "Visibility status updated successfully to " + user.getUserPlaylists().get(this.playlistId - 1).getTypePlaylist() + ".";
                } else { // inseamna ca el este privat acm si trebuie sa l fac public
                    user.getUserPlaylists().get(this.playlistId - 1).setTypePlaylist("public"); // deoarece este indexat de la 0
                    getAllUsersPlaylists().get(pozitiePlaylist).setTypePlaylist("public");
                    this.message = "Visibility status updated successfully to " + user.getUserPlaylists().get(this.playlistId - 1).getTypePlaylist() + ".";
                }
            }
            else
                this.message = "The specified playlist ID is too high.";
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
