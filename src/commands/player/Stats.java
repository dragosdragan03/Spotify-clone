package commands.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
//import fileio.input.*;

@JsonIgnoreProperties(value = {"username", "userHistory", "command", "timestamp"})
// pentru a ignora campurile mostenite
public class Stats extends CommandExecute {

    private String name = "";
    private int remainedTime = 0;
    private String repeat = "No Repeat";
    private boolean shuffle = false;
    private boolean paused = true;

    @JsonIgnoreProperties
    public Stats(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     *
     * @return numele userului
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return timpul ramas din melodie/episod
     */
    public int getRemainedTime() {
        return remainedTime;
    }

    /**
     *
     * @return daca este sau nu pe repeat userul meu
     * si returneaza valoarea 0/1/2 in functie de ce a fost setat
     */
    public String getRepeat() {
        return repeat;
    }

    /**
     * @return daca playlistul este sau nu pe shuffle
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     *
     * @return daca userul este pe play sau pe pause
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * aceasta metoda imi afiseaza statisticile userului
     */
    public void execute() {
        // retin sa vad ce user e folosit
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        FindTrack findTrack = new FindTrack(user, getTimestamp());
        findTrack.findTrackExecute();
        this.name = findTrack.getName();
        this.paused = findTrack.isPaused();
        this.remainedTime = findTrack.getRemainedTime();
        this.shuffle = findTrack.isShuffle();
        if (findTrack.getRepeat() == 0) {
            this.repeat = "No Repeat";
        } else if (findTrack.getRepeat() == 1) {
            if (user.getAudioFile().getPlaylistFile() != null
                    && user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) {
                this.repeat = "Repeat All";
            } else {
                this.repeat = "Repeat Once";
            }
        } else if (findTrack.getRepeat() == 2) {
            if (user.getAudioFile().getPlaylistFile() != null) {
                this.repeat = "Repeat Current Song";
            } else {
                this.repeat = "Repeat Infinite";
            }
        }
    }
}
