package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import main.Output;

public class Forward extends CommandExecute {

    private String message;

    public Forward(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /*fac o metoda pentru a vedea indexul episodului*/
    private int verifyNextEpisode(final PodcastInput podcast, final String nameEpisode) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            if (podcast.getEpisodes().get(i).getName().equals(nameEpisode)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * trece userul cu 90 de secunde inainte daca el mai are din episod mai mult de 90s
     * sau il trece la urmatorul episod daca el mai are de ascultat mai putin de 90s
     *
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            if (user.getAudioFile().getPodcastFile() != null) {
                final int timeForward = 90;
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute(); // sa vad la ce episod a ajuns in podcast
                // retin sa vad cat a mai ramas din episod
                int remainedTimeEpisode = findTrack.getRemainedTime();
                if (remainedTimeEpisode <= timeForward) { // inseamna ca ma duc in episodul urmator
                    int indexEpisode = verifyNextEpisode(user.getAudioFile().getPodcastFile(),
                            findTrack.getName());
                    if (indexEpisode == user.getAudioFile().getPodcastFile().
                            getEpisodes().size() - 1) {
                        // o sa am cazul in care este ultimul episod si sterg podcastul
                        eraseHistory();
                        this.message = "Please load a source before skipping forward.";
                    } else {
                        // inseamna ca este un episod oarecare si trebuie sa trec in episodul
                        // urmator de la inceput
                        user.setListeningTime(user.getListeningTime() + remainedTimeEpisode);
                        this.message = "Skipped forward successfully.";
                    }
                } else  { // inseamna ca doar avansez in timp la episodul curent
                    user.setListeningTime(user.getListeningTime() + timeForward);
                    this.message = "Skipped forward successfully.";
                }
            } else {
                this.message = "The loaded source is not a podcast.";
            }
        } else {
            this.message = "Please load a source before attempting to forward.";
        }
    }

    /**
     *
     * @return mesajul care a fost generat in metoda execute
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
