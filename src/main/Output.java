package main;

import com.fasterxml.jackson.annotation.JsonInclude;
import commands.player.Stats;

import java.util.ArrayList;

public class Output {
    private String command;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user;
    private Integer timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArrayList<String> results;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Stats stats;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArrayList<Object> result;

    /**
     * @param command   reprezinta comanda citita
     * @param user      numele userului
     * @param timestamp momentul de timp la care a fost data comanda
     */
    public Output(final String command, final String user, final Integer timestamp) {
        this.command = command;
        this.user = user;
        this.timestamp = timestamp;
    }

    /**
     * @param mssg    mesajul generat
     * @param strings arraylistul generat pentru comanda search
     */
    public void outputSearch(final String mssg, final ArrayList<String> strings) {
        this.message = mssg;
        this.results = strings;
    }

    /**
     * @param mssg mesajul care este generat in metodele execute
     */
    public void outputMessage(final String mssg) {
        this.message = mssg;
    }

    /**
     * @param status clasa status pentru comanda "status"
     */
    public void outputStatus(final Stats status) {
        this.stats = status;
    }

    /**
     * @return un arraylist de string/clase
     * acest getter a fost facut cu un arraylist de tip Object, deoarece
     * era o suprapunere de nume cu obiecte diferite
     */
    public ArrayList<Object> getResult() {
        return result;
    }

    /**
     * seteaza lista pentru a nu fi null
     */
    public void setResult() {
        if (this.result == null) {
            this.result = new ArrayList<>();
        }
    }

    /**
     * @return numele comenzii
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return numele utilizatorului
     */
    public String getUser() {
        return user;
    }

    /**
     * @return momentul de timp cand a fost data comanda
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     * @return mesajul generat
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return arraylist ul generat in clasa search
     */
    public ArrayList<String> getResults() {
        return results;
    }

    /**
     * @return clasa Status pentru comanda "stats"
     */
    public Stats getStats() {
        return stats;
    }
}
