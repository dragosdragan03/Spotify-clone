package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import main.Output;

public class AddRemoveInPlaylist extends CommandExecute {

    private int playlistId;
    private String message;

    public AddRemoveInPlaylist(Command command, LibraryInput library, int playlistId) {
        super(command, library);
        this.playlistId = playlistId;
    }

    private int verifySongExist(UserHistory user, String namePlaylist) {
        for (int i = 0; i < user.getUserPlaylists().get(this.playlistId - 1).getListSongs().size(); i ++) {// vreau sa parcurg lista de melodiii
            SongInput song = user.getUserPlaylists().get(this.playlistId - 1).getListSongs().get(i);
            if (song.getName().equals(namePlaylist)) // inseamnca ca nu numele exista in playlist
                return i; // inseamnca ca a gasit melodia mea in playlist

        }
        return -1; // inseamnca ca n a mai gasit melodia
    }

    @Override
    public void execute() {
        // vreau sa vad prima oara daca este incarcat ceva acum
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getUserPlaylists().size() >= this.playlistId) {// sa verific daca exista playlist ul user ului
            if (user.getTimeLoad() != 0) {// inseamna ca am incarcat ceva
                if (user.getAudioFile().getSong() != null) {// inseamnca ca e incarcat un song
                    if (user.getUserPlaylists().get(this.playlistId - 1).getListSongs() == null) { // inseamnca ca nu am bagat nicio melodie pana acm
                        SongInput song = user.getAudioFile().getSong();
                        user.getUserPlaylists().get(this.playlistId - 1).getListSongs().add(song);
                        this.message = "Successfully added to playlist.";
                    } else if (verifySongExist(user, user.getAudioFile().getSong().getName()) != -1) { // inseamna ca am gasit o melodie in playlist cu acelasi nume
                        int indexSongToRemove = verifySongExist(user, user.getAudioFile().getSong().getName());
                        user.getUserPlaylists().get(this.playlistId - 1).getListSongs().remove(indexSongToRemove);
                        this.message = "Successfully removed from playlist.";
                    } else if (verifySongExist(user, user.getAudioFile().getSong().getName()) == -1) { // inseamna ca nu am mai gasit o melodie cu ac nume
                        SongInput song = user.getAudioFile().getSong();
                        user.getUserPlaylists().get(this.playlistId - 1).getListSongs().add(song);
                        this.message = "Successfully added to playlist.";
                    }
                } else {
                    this.message = "The loaded source is not a song.";
                }
            } else
                this.message = "Please load a source before adding to or removing from the playlist.";
        } else
            this.message = "The specified playlist does not exist.";
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
