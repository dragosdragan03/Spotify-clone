package main;

import com.fasterxml.jackson.annotation.JsonInclude;
import commands.player.Stats;

import java.util.ArrayList;

public class Output {
    private String command;
    private String user;
    private Integer timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArrayList<String> results;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Stats stats;

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

    public String getCommand() {
        return command;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String username) {
        this.user = username;
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
