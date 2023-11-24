package commands.search;

//import commands.*;
import commands.Playlist;
import commands.Command;
import commands.CommandExecute;
import commands.FilterInput;
import commands.UserHistory;
import commands.player.FindTrack;
import fileio.input.EpisodeInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInputModified;
import fileio.input.SongInput;
import fileio.input.PodcastInput;
import main.Output;
import java.util.ArrayList;

public class Search extends CommandExecute {

    private String type;
    private FilterInput filter;
    // vreau sa retin in vectorul asta melodiile ce mi au rezultat
    private ArrayList<String> results = new ArrayList<>();
    private String message;
    private ArrayList<String> firstFive = new ArrayList<>();
    private final int maxSize = 5;

    public Search(final Command command, final LibraryInput library, final String type,
                  final FilterInput filter) {
        super(command, library);
        this.type = type;
        this.filter = filter;
    }

    /**
     *
     * @return arraylistul cu AudioFile urile gasite dupa filtrele cerute
     */
    public ArrayList<String> getResults() {
        return results;
    }

    /* fac o metoda pentru a retine podcastul si timpul pe care l a ascultat din podcast*/
    private void loadPodcast() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getAudioFile() != null) { // vreau sa vad daca a mai fost incarcat ceva pana acm
            // sa vad daca a fost incarcat un podcast inainte
            if (user.getAudioFile().getPodcastFile() != null) {
                String name = user.getAudioFile().getPodcastFile().getName();
                String owner = user.getAudioFile().getPodcastFile().getOwner();
                ArrayList<EpisodeInput> episodes = user.getAudioFile().getPodcastFile()
                        .getEpisodes();
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute();
                PodcastInputModified podcastInputModified =
                        new PodcastInputModified(name, owner, episodes, user.getListeningTime());
                user.getPastPodcast().add(podcastInputModified);
            }
        }
    }

    /**
     * aceasta metoda imi genereaza arraylistul de AudioFile uri cautate dupa filtrele citite
     */
    @Override
    public void execute() {
        loadPodcast();
        eraseHistory();
        this.results.clear();
        if (this.type.equals("song")) { // sa verific daca vreau sa caut o melodie
            // vreau sa parcurg toata lista de melodii
            for (SongInput var : this.library.getSongs()) {
                boolean detector = true;
                if (this.filter.getName() != null
                        && !var.getName().startsWith(this.filter.getName())) {
                    detector = false;
                }
                if (this.filter.getAlbum() != null
                        && !var.getAlbum().startsWith(this.filter.getAlbum())) {
                    detector = false;
                }
                if (this.filter.getTags().size() != 0
                        && !var.getTags().containsAll(this.filter.getTags())) {
                    detector = false;
                }
                if (this.filter.getLyrics() != null
                        && !var.getLyrics().toLowerCase().
                        contains(this.filter.getLyrics().toLowerCase())) {
                    detector = false;
                }
                if (this.filter.getGenre() != null
                        && !var.getGenre().toLowerCase().
                        contains(this.filter.getGenre().toLowerCase())) {
                    detector = false;
                }
                if (this.filter.getReleaseYear() != null) {
                    char sign = this.filter.getReleaseYear().charAt(0); // semnul stringului
                    if (sign == '>' && Integer.parseInt(this.filter.getReleaseYear().substring(1))
                            > var.getReleaseYear()) {
                        detector = false;
                    }
                    if (sign == '<' && Integer.parseInt(this.filter.getReleaseYear().substring(1))
                            < var.getReleaseYear()) {
                        detector = false;
                    }
                }
                if (this.filter.getArtist() != null
                        && !var.getArtist().equals(this.filter.getArtist())) {
                    detector = false;
                }

                if (detector) {
                    this.results.add(var.getName());
                }
            }
        } else if (this.type.equals("podcast")) {
            for (PodcastInput var : this.library.getPodcasts()) { // parcurg lista de podcasturi
                if (this.results.size() < maxSize) {
                    if (this.filter.getName() != null
                            && var.getName().startsWith(this.filter.getName())) {
                        this.results.add(var.getName());
                    }
                    if (this.filter.getOwner() != null
                            && var.getOwner().contains(this.filter.getOwner())) {
                        this.results.add(var.getName());
                    }
                }
            }
        } else if (type.equals("playlist")) {
            UserHistory user = getUserHistory().get(verifyUser(getUsername()));
            if (getAllUsersPlaylists().size() != 0) {
                for (Playlist var : getAllUsersPlaylists()) { // parcurg lista de playlisturi
                    if (this.filter.getName() != null
                            && var.getNamePlaylist().startsWith(filter.getName())
                            && (var.getTypePlaylist().equals("public")
                            || var.getUser().equals(user.getUser()))) {
                        this.results.add(var.getNamePlaylist());
                    }
                    if (filter.getOwner() != null && var.getUser().contains(filter.getOwner())
                            && (var.getTypePlaylist().equals("public")
                            || var.getUser().equals(user.getUser()))) {
                        this.results.add(var.getNamePlaylist());
                    }
                }
            }
        }
    } // melodiile rezultate

    /**
     *
     * @return arraylistul de String uri generat in metoda execute
     */
    @Override
    public Output generateOutput() {
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        for (String songs : this.results) {
            if (firstFive.size() < maxSize) {
                this.firstFive.add(songs);
            }
        }
        this.message = "Search returned " + this.firstFive.size() + " results";
        output.outputSearch(this.message, this.firstFive);
        return output;
    }

}
