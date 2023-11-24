package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class Repeat extends CommandExecute {

    private String message;

    public Repeat(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * seteaza userul pe repeat in functie de ce are incarcat podcast/song/playlist
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) {
            if (user.getAudioFile().getPlaylistFile() != null) {
                // inseamna ca am incarcat un playlist si vreau sa vad la ce melodie pun repeat
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute();
                if (findTrack.getRepeat() == 0) {
                    user.getAudioFile().getPlaylistFile().setRepeatPlaylist(1);
                    this.message = "Repeat mode changed to repeat all.";
                    return;
                } else if (findTrack.getRepeat() == 1) {
                    user.getAudioFile().getPlaylistFile().setRepeatPlaylist(2);
                    findTrack.getSongFound().setRepeat(2);
                    this.message = "Repeat mode changed to repeat current song.";
                    return;
                } else {
                    findTrack.getSongFound().setRepeat(0);
                    user.getAudioFile().getPlaylistFile().setRepeatPlaylist(0);
                    this.message = "Repeat mode changed to no repeat.";
                    return;
                }
            } else if (user.getAudioFile().getSongFile() != null) {
                // inseamna este incarcata o melodie
                if (user.getAudioFile().getSongFile().getRepeat() == 0) {
                    user.getAudioFile().getSongFile().setRepeat(1); // i am setat melodiei mele 1
                    this.message = "Repeat mode changed to repeat once.";
                    return;
                } else if (user.getAudioFile().getSongFile().getRepeat() == 1) {
                    // inseamna este incarcat un playlist si vreau sa vad la ce melodie pun repeat
                    FindTrack findTrack = new FindTrack(user, getTimestamp());
                    findTrack.findTrackExecute();
                    user.getAudioFile().getSongFile().setRepeat(2);
                    this.message = "Repeat mode changed to repeat infinite.";
                    return;
                } else {
                    FindTrack findTrack = new FindTrack(user, getTimestamp());
                    findTrack.findTrackExecute();
                    user.getAudioFile().getSongFile().setRepeat(0);
                    this.message = "Repeat mode changed to no repeat.";
                    return;
                }
            } else if (user.getAudioFile().getPodcastFile() != null) {
                // inseamna ca am incarcat un podcast
                if (user.getAudioFile().getRepeat() == 0) {
                    user.getAudioFile().setRepeat(1); // i am setat melodiei mele 1
                    this.message = "Repeat mode changed to repeat once.";
                    return;
                } else if (user.getAudioFile().getRepeat() == 1) {
                    user.getAudioFile().setRepeat(2);
                    this.message = "Repeat mode changed to repeat infinte";
                    return;
                } else {
                    user.getAudioFile().setRepeat(0);
                    this.message = "Repeat mode changed to no repeat.";
                    return;
                }
            }
        }
        this.message = "Please load a source before setting the repeat status.";
    }

    /**
     *
     * @return mesajul generat in metoda execute
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
