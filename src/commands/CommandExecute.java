package commands;

import commands.generals.GetTop5Playlists;
import commands.generals.GetTop5Songs;

import commands.player.Like;
import commands.player.Next;
import commands.player.Prev;
import commands.player.Forward;
import commands.player.Backward;
import commands.player.Repeat;
import commands.player.Shuffle;
import commands.player.ShowPreferredSongs;
import commands.player.AddRemoveInPlaylist;
import commands.player.FindTrack;
import commands.player.Load;
import commands.player.PlayPause;
import commands.player.Stats;
import commands.playlist.CreatePlaylist;
import commands.playlist.FollowPlaylist;
import commands.playlist.ShowPlaylists;
import commands.playlist.SwitchVisibility;
import commands.search.Search;
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
    // fac un arraylist cu playlisturule publice ale userilor
    private static ArrayList<Playlist> allUsersPlaylists = new ArrayList<>();

    /**
     * aceasta metoda sterge istoricul userilor si arraylistul de playlisturi create de useri
     */
    public static void clear() {
        userHistory = new ArrayList<>();
        allUsersPlaylists = new ArrayList<>();
    }

    public CommandExecute(final Command command, final LibraryInput library) {
        this.command = command.getCommand();
        this.username = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.library = library;
    }

    /**
     * aceasta metoda va fi suprascrisa si executa comanda care este citita
     */
    public void execute() {
    }

    /**
     * aceasta metoda va fi suprascrisa in fiecare clasa si are rolul de a afisa in fisierul Json
     */
    public Output generateOutput() {
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        return output;
    }

    public static ArrayList<Playlist> getAllUsersPlaylists() {
        return allUsersPlaylists;
    }

    /**
     * @return comanda care a fost citita
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return userul care a fost citit
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return momentul de timp cand a fost data comanda
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * @return arraylistul de useri
     */
    public ArrayList<UserHistory> getUserHistory() {
        return userHistory;
    }

    /**
     * aceasta metoda va fi mostenita si folosita in clasa search ca atunci cand se cauta ceva nou
     * sa se stearga tot
     */
    public void eraseHistory() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        user.setResultSearch(new ArrayList<>());
        user.setAudioFile(new AudioFile());
        user.setTimeLoad(0);
        user.setListeningTime(0);
        user.setPlayPauseResult(true);
    }

    /**
     * fac o metoda pentru a mi da update mereu la timpul ascultat
     */
    public void updateAudioFile() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        FindTrack findTrack = new FindTrack(user, getTimestamp());
        if (user.getTimeLoad() != 0) { // sa vad daca a fost incarcat ceva pana acm
            if (user.isPlayPauseResult()) { // inseamna ca e pe play
                // inseamna ca a ascultat incontinuu pana acm fara niciun pause
                if (user.getListeningTime() == 0) {
                    int firstLoad = user.getTimeLoad(); // retin de cand a inceput melodia
                    user.setListeningTime(getTimestamp() - firstLoad);
                    user.setTimeLoad(getTimestamp());

                    findTrack.findTrackExecute();
                    if (findTrack.getName() == "") {
                        eraseHistory();
                    }
                } else {
                    // retin cat timp a trecut de la ultimul play pana acm
                    int moreSeconds = getTimestamp() - user.getTimeLoad();
                    // adun timpul de ascultare pana acum cu cat a ascultat in plus
                    int secondsNow = user.getListeningTime();
                    user.setListeningTime(moreSeconds + secondsNow);
                    user.setTimeLoad(getTimestamp());

                    findTrack.findTrackExecute();
                    if (findTrack.getName() == "") {
                        eraseHistory();
                    }
                }
            } else { // inseamnca ca era pe pause si acm ii dau play
                user.setTimeLoad(getTimestamp()); // aici se reincepe play ul => incepe sa asculte

                findTrack.findTrackExecute();
                if (findTrack.getName() == "") {
                    eraseHistory();
                }
            }
        } else {
            user.setTimeLoad(0);
        }
    }

    /**
     *
     * @param user numele userului
     * @return indexul din lista de useri
     */
    public int verifyUser(final String user) {
        for (int i = 0; i < userHistory.size(); i++) {
            UserHistory iter = userHistory.get(i);
            if (user != null && iter.getUser().equals(user)) {
                return i; // Returnez indexul userului daca exista
            }
        }
        return -1; // Returnez -1 daca nu exista
    }

    /**
     * aceasta metoda imi executa comanda care a fost citita
     * @param cmd
     * @return clasa output (ce trebuie sa se afiseze)
     */
    public Output executeCommand(final Command cmd) {
        if (verifyUser(cmd.getUsername()) == -1) {
            this.userHistory.add(new UserHistory(cmd.getUsername()));
        }
        if (verifyUser(cmd.getUsername()) != -1) { // daca nu exista userul
            updateAudioFile();
        }
        switch (this.command) {
            case "search":
                Search search = new Search(cmd, this.library, cmd.getType(), cmd.getFilters());
                search.execute();
                userHistory.get(verifyUser(cmd.getUsername())).setResultSearch(search.getResults());
                return search.generateOutput();
            case "select":
                Select selectItem = new Select(cmd, this.library, cmd.getItemNumber());
                return selectItem.generateOutput();
            case "load":
                Load load = new Load(cmd, library);
                return load.generateOutput();
            case "playPause":
                PlayPause playPause = new PlayPause(cmd, library);
                return playPause.generateOutput();
            case "status":
                Stats status = new Stats(cmd, library);
                status.execute();
                Output output = new Output(getCommand(), getUsername(), getTimestamp());
                output.outputStatus(status);
                return output;
            case "addRemoveInPlaylist":
                AddRemoveInPlaylist addRemoveInPlaylist
                        = new AddRemoveInPlaylist(cmd, library, cmd.getPlaylistId());
                return addRemoveInPlaylist.generateOutput();
            case "createPlaylist":
                CreatePlaylist createPlaylist
                        = new CreatePlaylist(cmd, library, cmd.getPlaylistName());
                return createPlaylist.generateOutput();
            case "like":
                Like likeSong = new Like(cmd, library);
                return likeSong.generateOutput();
            case "showPlaylists":
                ShowPlaylists showPlaylists = new ShowPlaylists(cmd, library);
                return showPlaylists.generateOutput();
            case "showPreferredSongs":
                ShowPreferredSongs showPreferredSongs = new ShowPreferredSongs(cmd, library);
                return showPreferredSongs.generateOutput();
            case "repeat":
                Repeat repeat = new Repeat(cmd, library);
                return repeat.generateOutput();
            case "shuffle":
                Shuffle shuffle = new Shuffle(cmd, library, cmd.getSeed());
                return shuffle.generateOutput();
            case "switchVisibility":
                SwitchVisibility switchVisibility
                        = new SwitchVisibility(cmd, library, cmd.getPlaylistId());
                return switchVisibility.generateOutput();
            case "next":
                Next nextTrack = new Next(cmd, library);
                return nextTrack.generateOutput();
            case "prev":
                Prev prevTrack = new Prev(cmd, library);
                return prevTrack.generateOutput();
            case "forward":
                Forward forward = new Forward(cmd, library);
                return forward.generateOutput();
            case "backward":
                Backward backward = new Backward(cmd, library);
                return backward.generateOutput();
            case "follow":
                FollowPlaylist followPlaylist = new FollowPlaylist(cmd, library);
                return followPlaylist.generateOutput();
            case "getTop5Playlists":
                GetTop5Playlists getTop5Playlists = new GetTop5Playlists(cmd, library);
                return getTop5Playlists.generateOutput();
            case "getTop5Songs":
                GetTop5Songs getTop5Songs = new GetTop5Songs(cmd, library);
                return getTop5Songs.generateOutput();
            default:
                return null;
        }
    }
}
