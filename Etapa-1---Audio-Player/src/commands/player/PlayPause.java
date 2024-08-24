package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class PlayPause extends CommandExecute {

    private String message;

    public PlayPause() {
        super(new Command(), new LibraryInput());
    }

    public PlayPause(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * aceasta metoda da play/pause unui user indiferent de ce fisier a fost incarcat
     * si ii da update timpului de ascultare
     */
    @Override
    public void execute() {
        if (this.verifyUser(getUsername()) != -1) { // sa vad daca exista userul
            UserHistory user = getUserHistory().get(verifyUser(getUsername()));
            if (user.getTimeLoad() != 0) { // sa vad daca a fost incarcat ceva pana acm
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute();
                if (user.isPlayPauseResult()) { // inseamna ca era pe play deci pun pauza
                    user.setPlayPauseResult(false); // am pus pauza
                    this.message = "Playback paused successfully.";
                    return;
                } else { // inseamnca ca era pe pause si acm ii dau play
                    user.setPlayPauseResult(true); // inseama ca e pe pauza deci dau play
                    this.message = "Playback resumed successfully.";
                    return;
                }
            }
            this.message = "Please load a source before attempting to pause or resume playback.";
            return;
        }
        this.message = "Nu exista userul pentru PlayPause";
    }

    /**
     *
     * @return mesajul generat in metoda execute
     * daca userul a reinceput sa asculte sau s a oprit din ascultat
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
