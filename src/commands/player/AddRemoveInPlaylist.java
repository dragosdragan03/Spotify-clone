package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInputModified;
import main.Output;

public class AddRemoveInPlaylist extends CommandExecute {

    private int playlistId;
    private String message;

    public AddRemoveInPlaylist(final Command command, final LibraryInput library,
                               final int playlistId) {
        super(command, library);
        this.playlistId = playlistId;
    }

    private int verifySongExist(final UserHistory user, final String namePlaylist) {
        for (int i = 0; i < user.getUserPlaylists().get(this.playlistId - 1).getListSongs().size();
             i++) {
            SongInputModified song = user.getUserPlaylists().get(this.playlistId - 1).
                    getListSongs().get(i);
            if (song.getSong().getName().equals(namePlaylist)) {
                return i; // inseamnca ca a gasit melodia in playlist
            }
        }
        return -1; // inseamnca ca n a mai gasit melodia
    }

    /**
     * adauga in playlist o melodie
     * iar daca o gaseste deja in playlist o scoate
     */
    @Override
    public void execute() {
        // vreau sa vad prima oara daca este incarcat ceva acum
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        // sa verific daca exista playlist ul user ului
        if (user.getUserPlaylists().size() >= this.playlistId) {
            if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
                if (user.getAudioFile().getSongFile() != null) { // e incarcata o melodie
                    // inseamnca ca nu am adaugat nicio melodie
                    if (user.getUserPlaylists().get(this.playlistId - 1).getListSongs() == null) {
                        SongInputModified song = user.getAudioFile().getSongFile();
                        user.getUserPlaylists().get(this.playlistId - 1).getListSongs().add(song);
                        this.message = "Successfully added to playlist.";
                        // inseamna ca am gasit o melodie in playlist cu acelasi nume
                    } else if (verifySongExist(user, user.getAudioFile().getSongFile().
                            getSong().getName()) != -1) {
                        int indexSongToRemove = verifySongExist(user, user.getAudioFile().
                                getSongFile().getSong().getName());
                        user.getUserPlaylists().get(this.playlistId - 1).
                                getListSongs().remove(indexSongToRemove);
                        this.message = "Successfully removed from playlist.";
                        // inseamna ca nu am mai gasit o melodie cu ac nume
                    } else if (verifySongExist(user, user.getAudioFile().getSongFile().getSong().
                            getName()) == -1) {
                        SongInputModified song = user.getAudioFile().getSongFile();
                        user.getUserPlaylists().get(this.playlistId - 1).getListSongs().add(song);
                        this.message = "Successfully added to playlist.";
                    }
                } else {
                    this.message = "The loaded source is not a song.";
                }
            } else {
                this.message =
                        "Please load a source before adding to or removing from the playlist.";
            }
        } else {
            this.message = "The specified playlist does not exist.";
        }
    }

    /**
     *
     * @return mesajul generat in metoda execute
     * daca a fost adaugata melodia sau stearsa
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
