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
    private ArrayList<Object> result;

    public Output(String command, String user, Integer timestamp) {
        this.command = command;
        this.user = user;
        this.timestamp = timestamp;
    }

    public void outputSearch(String message, ArrayList<String> results) {
        this.message = message;
        this.results = results;
    }

    public void outputMessage(String message){
        this.message = message;
    }

    public void outputStatus(Stats status) {
        this.stats = status;
    }

    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult() {
        if (this.result == null)
            this.result = new ArrayList<>();
    }

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
