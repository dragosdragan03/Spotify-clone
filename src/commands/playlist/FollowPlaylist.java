package commands.playlist;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class FollowPlaylist extends CommandExecute {

    private String message;

    public FollowPlaylist(Command command, LibraryInput library) {
        super(command, library);
    }

    /*fac o metoda pentru a afla pe ce pozitie se afla playlistul meu in lista tuturor userilor*/
    private int indexPlaylist(Playlist playlist) {
        for (int i = 0; i <= getAllUsersPlaylists().size(); i++)
            if (getAllUsersPlaylists().get(i).equals(playlist))
                return i;
        return  -1;
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getAudioFile() != null && user.getResultSearch() == null) { // inseamna ca am incarcat ceva
            if (user.getAudioFile().getPlaylistFile() != null) {// inseamna ca am incarcat un playlist
                // prima oara trebuie sa verific daca playlistul pe care l am selectat este al meu
                if (user.getUserPlaylists().contains(user.getAudioFile().getPlaylistFile())) {// inseamna ca este unul din playlisturile mele
                    this.message = "You cannot follow or unfollow your own playlist.";
                } else { // daca nu l mai am in lista mea de playlisturi inseamna ca pot sa i dau fllw
                    if (user.getFollowPlaylists().contains(user.getAudioFile().getPlaylistFile())) { // inseamna ca deja il contine si trevuie scos de la fllw
                        user.getFollowPlaylists().remove(user.getAudioFile().getPlaylistFile()); // sterg din lista userului meu playlistul la care a dat fllw
                        int indexPlaylist = indexPlaylist(user.getAudioFile().getPlaylistFile());
                        int actualFollowers = getAllUsersPlaylists().get(indexPlaylist).getFollowers();
                        getAllUsersPlaylists().get(indexPlaylist).setFollowers(actualFollowers - 1);
                        this.message = "Playlist unfollowed successfully.";
                    } else { // inseamna ca nu contine playlistul
                        user.getFollowPlaylists().add(user.getAudioFile().getPlaylistFile()); // adaug in lista userului meu playlistul la care a dat fllw
                        int indexPlaylist = indexPlaylist(user.getAudioFile().getPlaylistFile());
                        int actualFollowers = getAllUsersPlaylists().get(indexPlaylist).getFollowers();
                        getAllUsersPlaylists().get(indexPlaylist).setFollowers(actualFollowers + 1);
                        this.message = "Playlist followed successfully.";
                    }
                }
            }
                else {
                    this.message= "The selected source is not a playlist.";
            }
        } else {
            this.message = "Please select a source before following or unfollowing.";
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
