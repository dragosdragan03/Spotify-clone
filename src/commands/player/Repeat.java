package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class Repeat extends CommandExecute {

    private String message;

    public Repeat(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername())); // retin userul la care am ramas
        if (user.getTimeLoad() != 0)
            if (user.getAudioFile().getPlaylistFile() != null) { // inseamna ca ruleaza un playlist acm
                FindTrack findTrack = new FindTrack(user, getTimestamp()); // inseamna ca am incarcat un playlist si vreau sa vad la ce melodie pun repeat
                findTrack.findTrackExecute();
                if (findTrack.getRepeat() == 0) {
                    user.getAudioFile().getPlaylistFile().setRepeatPlaylist(1); // am setat playlistul pe 1
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
            } else if (user.getAudioFile().getSongFile() != null) { // inseamna ca eu am incarcat o melodie acm
                if (user.getAudioFile().getSongFile().getRepeat() == 0) {
                    user.getAudioFile().getSongFile().setRepeat(1); // i am setat melodiei mele 1
                    this.message = "Repeat mode changed to repeat once.";
                    return;
                } else if (user.getAudioFile().getSongFile().getRepeat() == 1) {
//                user.setListeningTime(user.getListeningTime() - user.getAudioFile().getSongFile().getSong().getDuration());
                    FindTrack findTrack = new FindTrack(user, getTimestamp()); // inseamna ca am incarcat un playlist si vreau sa vad la ce melodie pun repeat
                    findTrack.findTrackExecute();
                    user.getAudioFile().getSongFile().setRepeat(2);
                    this.message = "Repeat mode changed to repeat infinite.";
                    return;
                } else {
                    FindTrack findTrack = new FindTrack(user, getTimestamp()); // inseamna ca am incarcat un playlist si vreau sa vad la ce melodie pun repeat
                    findTrack.findTrackExecute();
                    user.getAudioFile().getSongFile().setRepeat(0);
                    this.message = "Repeat mode changed to no repeat.";
                    return;
                }
            } else if (user.getAudioFile().getPodcastFile() != null) { // inseamna ca am incarcat un podcast
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
        this.message = "Please load a source before setting the repeat status.";
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
