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

    public SwitchVisibility(final Command command, final LibraryInput library,
                            final int playlistId) {
        super(command, library);
        this.playlistId = playlistId;
    }

    // fac o metoda pentru a mi returna playlistul userului din toata lista de playlisturi
    private int playlistPosition(final Playlist playlist) {
        // fac un for pentru a parcurge toata lista de playlisturi create
        for (int i = 0; i <= getAllUsersPlaylists().size(); i++) {
            if (getAllUsersPlaylists().get(i) == playlist) {
                return i; // returnez pozitia din vector pentru a l putea seta
            }
        }

        return -1;
    }

    /**
     * verific tipul playlistul cu indexul "this.playlistId - 1" si i schimb statusul din
     * public -> privat sau privat -> public dupa caz
     */
    @Override
    public void execute() {
        // retin userul sa vad pe ce user fac modificari
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getUserPlaylists() != null) { // sa vad daca am mai incarcat playlisturi in el
            if (user.getUserPlaylists().size() >= this.playlistId) {
                Playlist playlist = user.getUserPlaylists().get(this.playlistId - 1);
                int pozitiePlaylist = playlistPosition(playlist);
                // inseamna ca este un playlist public si vreau sa l fac privat
                if (playlist.getTypePlaylist().equals("public")) {
                    playlist.setTypePlaylist("private");
                    getAllUsersPlaylists().get(pozitiePlaylist).setTypePlaylist("private");
                    this.message = "Visibility status updated successfully to "
                            + playlist.getTypePlaylist() + ".";
                } else { // inseamna ca el este privat deci trebuie sa l fac public
                    playlist.setTypePlaylist("public");
                    getAllUsersPlaylists().get(pozitiePlaylist).setTypePlaylist("public");
                    this.message = "Visibility status updated successfully to "
                            + playlist.getTypePlaylist() + ".";
                }
            } else {
                this.message = "The specified playlist ID is too high.";
            }
        }
    }

    /**
     *
     * @return mesajul care mi s a generat in functia execute.
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
