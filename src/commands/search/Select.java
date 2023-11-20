package commands.search;

import commands.AudioFile;
import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import main.Output;

import java.util.ArrayList;

public class Select extends CommandExecute {

    private int itemNumber;
    private String message;
    private String audio;

    public Select(Command command, LibraryInput library, int itemNumber) {
        super(command, library);
        this.itemNumber = itemNumber;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    private PodcastInput returnPodcast(String name) { // vreau sa mi retuneze intregul podcast
        for (PodcastInput iter : library.getPodcasts())
            if (iter.getName().equals(name))
                return iter;
        return null;
    }

    private SongInput returnSong(String name) { // vreau sa mi returneze toata melodia
        for (SongInput iter : library.getSongs())
            if (iter.getName().equals(name))
                return iter;
        return null;
    }

    private Playlist returnPlaylist(String name) {
        for (Playlist iter : getAllUsersPlaylists()) // vreau sa parcurg toata lista de playlisturi publice
            if (iter.getNamePlaylist().equals(name) && iter.getTypePlaylist().equals("public")) // verific daca mi a gasit un playlist public
                return iter;
        return null;
    }

    // vreau sa selectez doar melodia din search
    @Override
    public void execute() {
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul mai intai
            if (getUserHistory().get(verifyUser(getUsername())).getResultSearch().size() == 0) { // sa vad daca a fost facut un search pana acum
                this.message = "Please conduct a search before making a selection.";
                return;
            }
            if (getUserHistory().get(verifyUser(getUsername())).getResultSearch().size() >= this.itemNumber) { // inseamna ca a fost facut un search
                this.audio = getUserHistory().get(verifyUser(getUsername())).getResultSearch().get(itemNumber - 1);
                if (returnPodcast(this.audio) != null) { // daca este podcast
                    AudioFile audioFile = new AudioFile(returnPodcast(this.audio), "podcast"); // selectez un podcast
                    getUserHistory().get(verifyUser(getUsername())).setAudioFile(audioFile);
                } else if (returnSong(this.audio) != null) {
                    AudioFile audioFile = new AudioFile(returnSong(this.audio), "song"); // selectez o melodie
                    getUserHistory().get(verifyUser(getUsername())).setAudioFile(audioFile);
                } else if (returnPlaylist(this.audio) != null) {
                    AudioFile audioFile = new AudioFile(returnPlaylist(this.audio), "playlist"); // aici selectez un playlist
                    getUserHistory().get(verifyUser(getUsername())).setAudioFile(audioFile);
                }
                getUserHistory().get(verifyUser(getUsername())).setResultSearch(new ArrayList<String>()); // trebuie sa sterg search ul care este acum
                this.message = "Successfully selected " + this.audio + ".";
                return;
            } else {
                this.message = "The selected ID is too high.";
                return;
            }
        }
        this.message = "Nu exista userul";
    }

    @Override
    public Output generateOutput() {
        this.execute();
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
