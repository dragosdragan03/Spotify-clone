package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInputModified;
import main.Output;

import javax.print.attribute.standard.Fidelity;

public class Next extends CommandExecute {

    private String message;

    public Next(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            FindTrack findTrack = new FindTrack(user, getTimestamp());
            findTrack.findTrackExecute();
            if (user.getAudioFile().getPlaylistFile() != null) {
                // trebuie sa verific daca
                int remainedTime = findTrack.getRemainedTime(); // verific cat timp mi a mai ramas din melodia din playlist
                user.setListeningTime(user.getListeningTime() + remainedTime); // vreau sa consider ca deja am ascultat melodia
                FindTrack newFindTrack = new FindTrack(user, getTimestamp() + remainedTime);
                newFindTrack.findTrackExecute(); // ii dau sa mai execute o data sa vad daca mai gaseste ceva dupa sau nu
                if (newFindTrack.getSongFound() != null) {
                    user.setPlayPauseResult(true);
                    this.message = "Skipped to next track successfully. The current track is " + newFindTrack.getName() + ".";
                } else {
                    this.message = "Please load a source before skipping to the next track."; // inseamna ca nu mi a mai gasit nimci dupa deci trebuie sa l sterg
                    eraseHistory();
                }
            } else if (user.getAudioFile().getSongFile() != null) {
                // am trei cazuri de tratat
                // 1. daca nu e pe repeat; 2. daca e pe repeat once; 3. daca e pe infinit
                if (user.getAudioFile().getSongFile().getRepeat() == 0) {
                    this.message = "Please load a source before skipping to the next track.";
                    eraseHistory(); // inseamna ca nu mai ruleaza nimic
                } else if (user.getAudioFile().getRepeat() == 1) { // inseamna ca mai are putin de rulat din melodia asta si trece la urmatoarea
                    user.getAudioFile().setRepeat(0); // trece la urmatorae melodie si nu mai ruleaza nimic
                    int remainedTimeSong = findTrack.getRemainedTime(); // inseamna ca atat a mai ramas de acultat din melodie
                    user.setListeningTime(user.getListeningTime() + remainedTimeSong); // trec cu timpul la urmatoarea melodie
                    this.message = "Skipped to next track successfully. The current track is " + findTrack.getName() + ".";
                } else if (user.getAudioFile().getRepeat() == 2) {
                    int remainedTimeSong = findTrack.getRemainedTime(); // inseamna ca atat a mai ramas de acultat din melodie
                    user.setListeningTime(user.getListeningTime() + remainedTimeSong);
                    this.message = "Skipped to next track successfully. The current track is " + findTrack.getName() + ".";
                }
            } else if (user.getAudioFile().getPodcastFile() != null) {
                int remainedTime = findTrack.getRemainedTime();
                user.setListeningTime(user.getListeningTime() + remainedTime);
                FindTrack newFindTrack = new FindTrack(user, getTimestamp() + remainedTime);
                newFindTrack.findTrackExecute();
                if (newFindTrack.getEpisodeFound() != null) { // inseamna ca nu s a terminat podcastul
                    this.message = "Skipped to next track successfully. The current track is " + newFindTrack.getEpisodeFound().getName() + ".";
                } else { // inseamna ca nu mai exista niciun episod dupa deci mi s a terminat podcastul
                    this.message = "Please load a source before skipping to the next track."; // inseamna ca nu mi a mai gasit nimci dupa deci trebuie sa l sterg
                    eraseHistory();
                }
            }
        } else {
            this.message = "Please load a source before skipping to the next track.";
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
