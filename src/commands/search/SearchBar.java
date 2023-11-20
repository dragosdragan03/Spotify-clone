package commands.search;

import com.sun.management.DiagnosticCommandMBean;
import commands.*;
import commands.FilterInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import main.Output;

import java.util.ArrayList;

public class SearchBar extends CommandExecute {

    private String type;
    private FilterInput filter;
    private ArrayList<String> results = new ArrayList<>(); // vreau sa retin in vectorul asta melodiile ce mi au rezultat
    private String message;
    private ArrayList<String> firstFive = new ArrayList<>();

    public SearchBar(Command command, LibraryInput library, String type, FilterInput filter) {
        super(command, library);
        this.type = type;
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FilterInput getFilter() {
        return filter;
    }

    public void setFilter(FilterInput filter) {
        this.filter = filter;
    }

    public ArrayList<String> getResults() {
        return results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }

    // fac o metoda pentru a retine ce ce melodii/podcasturi/playlist uri sunt alese
    private void eraseHistory() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        user.setResultSearch(new ArrayList<>());
        user.setAudioFile(new AudioFile());
        user.setTimeLoad(0);
        user.setListeningTime(0);
        user.setPlayPauseResult(true);
    }
    @Override
    public void execute() {
        eraseHistory();
        this.results.clear();
        if (this.type.equals("song")) { // sa verific daca vreau sa caut o melodie
            for (SongInput var : this.library.getSongs()) { // vreau sa parcurg toata lista de cantece
                     boolean detector = true;
                    if (this.filter.getName() != null && !var.getName().startsWith(this.filter.getName())) // vreau sa vad daca filtrul meu este in cantec
                        detector = false;
                    if (this.filter.getAlbum() != null && !var.getAlbum().startsWith(this.filter.getAlbum()))
                        detector = false;
                    if (this.filter.getTags().size() != 0 && !var.getTags().containsAll(this.filter.getTags()))
                        detector = false;
                    if (this.filter.getLyrics() != null && !var.getLyrics().contains(this.filter.getLyrics()))
                        detector = false;
                    if (this.filter.getGenre() != null && !var.getGenre().toLowerCase().contains(this.filter.getGenre().toLowerCase()))
                        detector = false;
                    if (this.filter.getReleaseYear() != null) {
                        char sign = this.filter.getReleaseYear().charAt(0); // semnul stringului
                        if (sign == '>' && Integer.parseInt(this.filter.getReleaseYear().substring(1)) > var.getReleaseYear())
                            detector = false;
                        if (sign == '<' && Integer.parseInt(this.filter.getReleaseYear().substring(1)) < var.getReleaseYear())
                            detector = false;
                    }
                    if (this.filter.getArtist() != null && !var.getArtist().equals(this.filter.getArtist()))
                        detector = false;

                    if (detector)
                        this.results.add(var.getName());
            }
        } else if (this.type.equals("podcast")) {
            for (PodcastInput var : this.library.getPodcasts()) { // vreau sa parcurg toata lista de podcasturi
                if (this.results.size() < 5) {
                    if (this.filter.getName() != null && var.getName().startsWith(this.filter.getName())) // vreau sa vad daca filtrul meu este in cantec
                        this.results.add(var.getName());
                    if (this.filter.getOwner() != null && var.getOwner().contains(this.filter.getOwner()))
                        this.results.add(var.getName());
                }
            }
         } else if (type.equals("playlist")) {
            if (getAllUsersPlaylists().size() != 0){
                for (Playlist var : getAllUsersPlaylists()) { // vreau sa parcurg toata lista de playlisturi
                    if (this.filter.getName() != null && var.getNamePlaylist().startsWith(filter.getName()) && var.getTypePlaylist().equals("public")) // vreau sa vad daca filtrul meu este in cantec
                     this.results.add(var.getNamePlaylist());
                    if (filter.getOwner() != null && var.getUser().contains(filter.getOwner()) && var.getTypePlaylist().equals("public"))
                        this.results.add(var.getNamePlaylist());
            }
            }
        }
   } // melodiile rezultate

    @Override
    public Output generateOutput() {
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        for (String songs : this.results){
            if (firstFive.size() < 5)
                this.firstFive.add(songs);
        }
        this.message = "Search returned " + this.firstFive.size() + " results";
        output.outputSearch(this.message, this.firstFive);
        return output;
    }

}
