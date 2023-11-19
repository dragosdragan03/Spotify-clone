package main;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.HashMap;
import commands.player.Stats;
import commands.playlist.ShowPlaylists;

import java.util.ArrayList;

public class Output {
    private String command;
    private String user;
    private Integer timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArrayList<String> results; // pe asta il folosesc in primul test
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Stats stats;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, ArrayList<?>> result;
    //    private ArrayList<ShowPlaylists.PlaylistShow> result;

    public Output(String command, String user, Integer timestamp) {
        this.command = command;
        this.user = user;
        this.timestamp = timestamp;
    }

    public void outputSearch(String message, ArrayList<String> results) {
        this.message = message;
        this.results = results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }

    public void outputMessage(String message){
        this.message = message;
    }

    public void outputStatus(Stats status) {
        this.stats = status;
    }

//    public Map<String, ArrayList<?>> getResult() {
//        return result;
//    }

    public void setResultPlylist(ArrayList<ShowPlaylists.PlaylistShow> playlist) {
        if (this.result == null)
        this.result = new HashMap<>();
        this.result.put("playlist", playlist);
    }

    public void setResultPreferredSongs(ArrayList<String> songs) {
        if (this.result == null)
        this.result = new HashMap<>();
        this.result.put("song", songs);
    }

    public ArrayList<?> getResult() {
        if (this.result != null) { // inseamnca ca e un arraylist de tip string
            return (ArrayList<?>) this.result; // This is an ArrayList<String>
        } else {
            return (ArrayList<?>) this.result; // This is an ArrayList<ShowPlaylists.PlaylistShow>
        }
    }

    //    public void outputPlaylist(ArrayList<ShowPlaylists.PlaylistShow> result) {
//        this.result = result;
//    }
//    public ArrayList<ShowPlaylists.PlaylistShow> getResult() {
//        return result;
//    }

    public String getCommand() {
        return command;
    }

    public String getUser() {
        return user;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getResults() {
        return results;
    }

    public Stats getStats() {
        return stats;
    }
}
