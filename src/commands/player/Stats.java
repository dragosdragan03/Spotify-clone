package commands.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;

@JsonIgnoreProperties(value = { "username", "userHistory", "command", "timestamp" }) // pentru a ignora campurile mostenite
public class Stats extends CommandExecute {

    private String name = "";
    private int remainedTime = 0;
    private String repeat = "No Repeat";
    private boolean shuffle = false;
    private boolean paused = true;

    @JsonIgnoreProperties
    public Stats(Command command, LibraryInput library) {
        super(command, library);
    }

    public String getName() {
        return name;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public boolean isPaused() {
        return paused;
    }

    public void execute() {
        // vreau sa vad user ul meu ce comenzi a fct pana acm
        UserHistory user = getUserHistory().get(verifyUser(getUsername())); // retin sa vad ce user am
        if (user.getAudioFile().getPodcast() != null) {// sa vad daca am retinut un podcas sau un song

        } else if (user.getAudioFile().getSong() != null) { // inseamna ca am incarcat un song
            // prima oara trebuie sa verific cat timp are song ul meu si cat timp a mai ramas din el
            if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                if (user.isPlayPauseResult() == false) {// inseamnca ca e pe pauza si retin direct cat a ascultat
                    int listeningTime = user.getListeningTime();
                    int timeSong = user.getAudioFile().getSong().getDuration();
                    if (timeSong - listeningTime >= 0) {// inseamna ca inca mai are de ascultat
                        this.remainedTime = timeSong - listeningTime;
                        this.name = user.getAudioFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    }
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    int lastPlay = user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int listeningTime = user.getListeningTime();
                    int timeSong = user.getAudioFile().getSong().getDuration();
                    if (timeSong - (listeningTime + (getTimestamp() - lastPlay)) >= 0) {// inse
                        this.remainedTime = timeSong - (listeningTime + (getTimestamp() - lastPlay));
                        this.name = user.getAudioFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    }
                }
            } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = getTimestamp() - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                int timeSong = user.getAudioFile().getSong().getDuration();
                if (timeSong - listeningTime >= 0) {// inseamna ca inca mai e timp se ascultat
                    this.remainedTime = timeSong - listeningTime;
                    this.name = user.getAudioFile().getSong().getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                }
            }
        }
    }
}
