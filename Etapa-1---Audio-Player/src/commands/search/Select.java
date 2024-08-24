package commands.search;
import commands.AudioFile;
import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.SongInputModified;
import main.Output;

public class Select extends CommandExecute {

    private int itemNumber;
    private String message;
    private String audio;

    public Select(final Command command, final LibraryInput library, final int itemNumber) {
        super(command, library);
        this.itemNumber = itemNumber;
    }

    /**
     * @param name podcast
     * @return podcastul gasit in lista de podcasturi
     */
    private PodcastInput returnPodcast(final String name) {
        for (PodcastInput iter : library.getPodcasts()) {
            if (iter.getName().equals(name)) {
                return iter;
            }
        }
        return null;
    }

    private SongInput returnSong(final String name) { // vreau sa mi returneze toata melodia
        for (SongInput iter : library.getSongs()) {
            if (iter.getName().equals(name)) {
                return iter;
            }
        }
        return null;
    }

    private Playlist returnPlaylist(final String name) {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        for (Playlist iter : getAllUsersPlaylists()) { // parcurg lista de playlisturi publice
            // verific daca a gasit un playlist public
            if (iter.getNamePlaylist().equals(name)
                    && (iter.getTypePlaylist().equals("public")
                    || iter.getUser().equals(user.getUser()))) {
                return iter;
            }
        }
        return null;
    }

    /**
     * vreau sa selectez fisierul cu indexul itemNumber din search
     */
    @Override
    public void execute() {
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul mai intai

            UserHistory user = getUserHistory().get(verifyUser(getUsername()));
            if (user.getResultSearch() == null) { // verific daca a fost facut un search pana acum
                this.message = "Please conduct a search before making a selection.";
                return;
            }
            if (user.getResultSearch().size() >= this.itemNumber) {
                this.audio = user.getResultSearch().get(itemNumber - 1);
                if (returnPodcast(this.audio) != null) { // daca este podcast il selectez
                    AudioFile audioFile = new AudioFile(returnPodcast(this.audio), "podcast");
                    user.setAudioFile(audioFile);
                } else if (returnSong(this.audio) != null) { // selectez o melodie
                    SongInputModified song = new SongInputModified(returnSong(this.audio), 0);
                    AudioFile audioFile = new AudioFile(song, "song");
                    user.setAudioFile(audioFile);
                } else if (returnPlaylist(this.audio) != null) {  // selectez un playlist
                    AudioFile audioFile = new AudioFile(returnPlaylist(this.audio), "playlist");
                    user.setAudioFile(audioFile);
                }
                user.setResultSearch(null); // trebuie sa sterg search ul care este acum
                this.message = "Successfully selected " + this.audio + ".";
                return;
            } else {
                this.message = "The selected ID is too high.";
                return;
            }
        }
        this.message = "Nu exista userul";
    }

    /**
     *
     * @return mesajul care s a creat in metoda execute
     */
    @Override
    public Output generateOutput() {
        this.execute();
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
