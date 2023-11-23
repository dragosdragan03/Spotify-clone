package commands;

import commands.generals.GetTop5Playlists;
import commands.generals.GetTop5Songs;
import commands.player.*;
import commands.playlist.CreatePlaylist;
import commands.playlist.FollowPlaylist;
import commands.playlist.ShowPlaylists;
import commands.playlist.SwitchVisibility;
import commands.search.SearchBar;
import commands.search.Select;
import fileio.input.LibraryInput;
import main.Output;

import java.util.ArrayList;

public class CommandExecute {

    private String command;
    private String username;
    private int timestamp;
    private static ArrayList<UserHistory> userHistory = new ArrayList<>();
    protected LibraryInput library;
    private static ArrayList<Playlist> allUsersPlaylists = new ArrayList<>(); // fac un arraylist cu playlisturule publice ale userilor

    public static void clear() {
        userHistory = new ArrayList<>();
        allUsersPlaylists = new ArrayList<>();
    }

    public CommandExecute(Command command, LibraryInput library) {
        this.command = command.getCommand();
        this.username = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.library = library;
    }

    public void execute() {
    }

    public Output generateOutput() {
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        return output;
    }

    public static ArrayList<Playlist> getAllUsersPlaylists() {
        return allUsersPlaylists;
    }

    public String getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public ArrayList<UserHistory> getUserHistory() {
        return userHistory;
    }

    public void eraseHistory() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        user.setResultSearch(new ArrayList<>());
        user.setAudioFile(new AudioFile());
        user.setTimeLoad(0);
        user.setListeningTime(0);
        user.setPlayPauseResult(true);
    }

    /*fac o functie pentru a mi da update mereu la AudioFile*/
    public void updateAudioFile() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        FindTrack findTrack = new FindTrack(user, getTimestamp());
        if (user.getTimeLoad() != 0) { // sa vad daca a fost incarcat ceva pana acm
            if (user.isPlayPauseResult()) { // inseamna ca e pe play
                if (user.getListeningTime() == 0) { // inseamna ca a mers incontinuu pana acm fara niciun pause
                    int firstLoad = user.getTimeLoad(); // retin de cand a inceput melodia
                    user.setListeningTime(getTimestamp() - firstLoad); // inseamna ca a ascultat pana acm
                    user.setTimeLoad(getTimestamp());

                    findTrack.findTrackExecute();
                    if (findTrack.getName() == "")
                        eraseHistory();
                } else {
                    int moreSeconds = getTimestamp() - user.getTimeLoad(); // retin cat timp a trecut de la ultimul play pana acm
                    int secondsNow = user.getListeningTime(); // ca sa adun timpul de ascultare pana acum cu cat a ascultat in plus
                    user.setListeningTime(moreSeconds + secondsNow);
                    user.setTimeLoad(getTimestamp());

                    findTrack.findTrackExecute();
                    if (findTrack.getName() == "")
                        eraseHistory();
                }
            } else { // inseamnca ca era pe pause si acm ii dau play
                user.setTimeLoad(getTimestamp()); // aici se reincepe play ul => incepe sa asculte iar

                findTrack.findTrackExecute();
                if (findTrack.getName() == "")
                    eraseHistory();
            }
        } else
            user.setTimeLoad(0);
    }

    public int verifyUser(String user) {
        for (int i = 0; i < userHistory.size(); i++) {
            UserHistory iter = userHistory.get(i);
            if (user != null && iter.getUser().equals(user)) {
                return i; // Returnez indexul userului daca exista
            }
        }
        return -1; // Returnez -1 daca nu exista
    }

    public Output executeCommand(Command command) {
        if (verifyUser(command.getUsername()) == -1)
            this.userHistory.add(new UserHistory(command.getUsername()));
        if (verifyUser(command.getUsername()) != -1) // daca nu exista
            updateAudioFile();
        switch (this.command) {
            case "search":
                SearchBar search = new SearchBar(command, this.library, command.getType(), command.getFilters());
                search.execute();
                userHistory.get(verifyUser(command.getUsername())).setResultSearch(search.getResults());
                return search.generateOutput();
            case "select":
                Select selectItem = new Select(command, this.library, command.getItemNumber());
                return selectItem.generateOutput();
            case "load":
                Load load = new Load(command, library);
                return load.generateOutput();
            case "playPause":
                PlayPause playPause = new PlayPause(command, library);
                return playPause.generateOutput();
            case "status":
                Stats status = new Stats(command, library);
                status.execute();
                Output output = new Output(getCommand(), getUsername(), getTimestamp());
                output.outputStatus(status);
                return output;
            case "addRemoveInPlaylist":
                AddRemoveInPlaylist addRemoveInPlaylist = new AddRemoveInPlaylist(command, library, command.getPlaylistId());
                return addRemoveInPlaylist.generateOutput();
            case "createPlaylist":
                CreatePlaylist createPlaylist = new CreatePlaylist(command, library, command.getPlaylistName());
                return createPlaylist.generateOutput();
            case "like":
                Like likeSong = new Like(command, library);
                return likeSong.generateOutput();
            case "showPlaylists":
                ShowPlaylists showPlaylists = new ShowPlaylists(command, library);
                return showPlaylists.generateOutput();
            case "showPreferredSongs":
                ShowPreferredSongs showPreferredSongs = new ShowPreferredSongs(command, library);
                return showPreferredSongs.generateOutput();
            case "repeat":
                Repeat repeat = new Repeat(command, library);
                return repeat.generateOutput();
            case "shuffle":
                Shuffle shuffle = new Shuffle(command, library, command.getSeed());
                return shuffle.generateOutput();
            case "switchVisibility":
                SwitchVisibility switchVisibility = new SwitchVisibility(command, library, command.getPlaylistId());
                return switchVisibility.generateOutput();
            case "next":
                Next nextTrack = new Next(command, library);
                return nextTrack.generateOutput();
            case "prev":
                Prev prevTrack = new Prev(command, library);
                return prevTrack.generateOutput();
            case "forward":
                Forward forward = new Forward(command, library);
                return forward.generateOutput();
            case "backward":
                Backward backward = new Backward(command, library);
                return backward.generateOutput();
            case "follow":
                FollowPlaylist followPlaylist = new FollowPlaylist(command, library);
                return followPlaylist.generateOutput();
            case "getTop5Playlists":
                GetTop5Playlists getTop5Playlists = new GetTop5Playlists(command, library);
                return getTop5Playlists.generateOutput();
            case "getTop5Songs":
                GetTop5Songs getTop5Songs = new GetTop5Songs(command, library);
                return getTop5Songs.generateOutput();
        }
        return null;
    }
}
