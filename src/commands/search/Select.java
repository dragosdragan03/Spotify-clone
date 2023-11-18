package commands.search;

import commands.AudioFile;
import commands.Command;
import commands.CommandExecute;
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

    // vreau sa selectez doar melodia din search
    @Override
    public void execute() {
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul mai intai
            if (getUserHistory().get(verifyUser(getUsername())).getResultSearch().size() == 0) {
                this.message = "Please conduct a search before making a selection.";
                return;
            }
            if (getUserHistory().get(verifyUser(getUsername())).getResultSearch().size() >= this.itemNumber) {
                this.audio = getUserHistory().get(verifyUser(getUsername())).getResultSearch().get(itemNumber - 1);
                if (returnPodcast(this.audio) != null) {// daca este podcast
                    AudioFile audioFile = new AudioFile(returnPodcast(this.audio), "podcast");
                    getUserHistory().get(verifyUser(getUsername())).setAudioFile(audioFile);
                } else if (returnSong(this.audio) != null) {
                    AudioFile audioFile = new AudioFile(returnSong(this.audio), "song");
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
