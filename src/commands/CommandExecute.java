package commands;

import commands.player.Load;
import commands.player.PlayPause;
import commands.player.Stats;
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
//                System.out.println(userHistory.get(verifyUser(command.getUsername())).getAudioFile().getSong().getName());
//                System.out.println(userHistory.get(verifyUser(command.getUsername())).isPlayPauseResult());
//                System.out.println(userHistory.get(verifyUser(command.getUsername())).getListeningTime());
                return playPause.generateOutput();
            case "status":
                Stats status = new Stats(command, library);
                status.execute();
                Output output = new Output(getCommand(), getUsername(), getTimestamp());
                output.outputStatus(status);
                return output;
            case "repeat":

            case "shuffle":

            case "forward":

            case "backward":

            case "like":

            case "next":

            case "prev":

            case "addRemoveInPlaylist":

            case "createPlaylist":

            case "switchVisibility":

            case "follow":

            case "showPlaylists":

            case "showPreferredSongs":

            case "getTop5Songs":

            case "getTop5Playlists":

        }
        return null;
    }
}
