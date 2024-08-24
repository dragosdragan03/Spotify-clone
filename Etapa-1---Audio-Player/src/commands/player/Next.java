package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class Next extends CommandExecute {

    private String message;

    public Next(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * aceasta metoda trimite userul la urmatoarea melodie/urmatorul episod
     * in cazul in care nu mai exista o sa fie sters
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            FindTrack findTrack = new FindTrack(user, getTimestamp());
            findTrack.findTrackExecute();
            if (user.getAudioFile().getPlaylistFile() != null) {
                // verific cat timp a mai ramas din melodia din playlist
                int remainedTime = findTrack.getRemainedTime();
                // consider ca deja am ascultat melodia
                user.setListeningTime(user.getListeningTime() + remainedTime);
                FindTrack newFindTrack = new FindTrack(user, getTimestamp() + remainedTime);
                // mai executa o data sa vad daca mai gaseste ceva dupa sau nu
                newFindTrack.findTrackExecute();
                if (newFindTrack.getSongFound() != null) {
                    user.setPlayPauseResult(true);
                    this.message = "Skipped to next track successfully. The current track is "
                            + newFindTrack.getName() + ".";
                } else {
                    // inseamna ca nu a mai gasit nimic dupa deci trebuie sa l sterg
                    this.message = "Please load a source before skipping to the next track.";
                    eraseHistory();
                }
            } else if (user.getAudioFile().getSongFile() != null) {
                // 1. daca nu e pe repeat; 2. daca e pe repeat once; 3. daca e pe infinit
                if (user.getAudioFile().getSongFile().getRepeat() == 0) {
                    this.message = "Please load a source before skipping to the next track.";
                    eraseHistory(); // inseamna ca nu mai ruleaza nimic
                } else if (user.getAudioFile().getRepeat() == 1) {
                    // inseamna ca mai are putin de ascultat din melodie si trece la urmatoarea
                    user.getAudioFile().setRepeat(0); // nu mai e pe repeat
                    // inseamna ca atat a mai ramas de acultat din melodie
                    int remainedTimeSong = findTrack.getRemainedTime();
                    user.setListeningTime(user.getListeningTime() + remainedTimeSong);
                    this.message = "Skipped to next track successfully. The current track is "
                            + findTrack.getName() + ".";
                } else if (user.getAudioFile().getRepeat() == 2) {
                    // inseamna ca atat a mai ramas de acultat din melodie
                    int remainedTimeSong = findTrack.getRemainedTime();
                    user.setListeningTime(user.getListeningTime() + remainedTimeSong);
                    this.message = "Skipped to next track successfully. The current track is "
                            + findTrack.getName() + ".";
                }
            } else if (user.getAudioFile().getPodcastFile() != null) {
                int remainedTime = findTrack.getRemainedTime();
                user.setListeningTime(user.getListeningTime() + remainedTime);
                FindTrack newFindTrack = new FindTrack(user, getTimestamp() + remainedTime);
                newFindTrack.findTrackExecute();
                if (newFindTrack.getEpisodeFound() != null) { // nu s a terminat podcastul
                    this.message = "Skipped to next track successfully. The current track is "
                            + newFindTrack.getEpisodeFound().getName() + ".";
                } else { // inseamna ca nu mai exista niciun episod dupa => s a terminat podcastul
                    this.message = "Please load a source before skipping to the next track.";
                    // inseamna ca nu mi a mai gasit nimic dupa deci trebuie sa l sterg
                    eraseHistory();
                }
            }
        } else {
            this.message = "Please load a source before skipping to the next track.";
        }
    }

    /**
     *
     * @return mesajul generat in metoda execute + melodia/episodul la care a trecut
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
