package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class PlayPause extends CommandExecute {

    private String message;

    public PlayPause(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul mai intai
            UserHistory user = getUserHistory().get(verifyUser(getUsername()));
            if (user.getTimeLoad() != 0) { // sa vad daca a fost incarcat ceva pana acm
                    if (user.isPlayPauseResult()) { // inseamna ca era pe play si i pun pauza
                        if (user.getListeningTime() == 0) {// inseamna ca a mers incontinuu pana acm
                            int firstLoad = user.getTimeLoad(); // retin de cand a inceput melodia
                            user.setListeningTime(getTimestamp() - firstLoad); // inseamna ca a ascultat pana acm
                            user.setTimeLoad(getTimestamp());
                        } else {
                            int moreSeconds = getTimestamp() - user.getTimeLoad(); // retin cat timp a trecut de la ultimul play pana acm
                            int secondsNow = user.getListeningTime(); // ca sa adun timpul de ascultare pana acum cu cat a ascultat in plus
                            user.setListeningTime(moreSeconds + secondsNow);
                            user.setTimeLoad(getTimestamp());
                        }
                        user.setPlayPauseResult(false); // am pus pauza
                        this.message = "Playback paused successfully.";
                        return;
                    } else { // inseamnca ca era pe pause si acm ii dau play
                        user.setTimeLoad(getTimestamp()); // aici se reincepe play ul => incepe sa asculte iar
                        user.setPlayPauseResult(true); // inseama ca e pe pauza si i dau play
                        this.message = "Playback resumed successfully.";
                        return;
                    }
            }
            this.message = "Please load a source before attempting to pause or resume playback.";
            return;
            }
        this.message = "Nu exista userul pentru PlayPause";
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
