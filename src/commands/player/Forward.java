package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import main.Output;

public class Forward extends CommandExecute {

    private String message;

    public Forward(Command command, LibraryInput library) {
        super(command, library);
    }

    /*fac o metoda pentru a vedea indexul episodului meu*/
    private int verifyNextEpisode(PodcastInput podcast, String nameEpisode) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++)
            if (podcast.getEpisodes().get(i).getName().equals(nameEpisode))
                return i;
        return -1;
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            if (user.getAudioFile().getPodcastFile() != null) { // inseamna ca am incarcat un playlist
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute(); // sa vad unde se alfa prin podcast
                int remainedTimeEpisode = findTrack.getRemainedTime(); // retin sa vad cat a mai ramas din episod
                if (remainedTimeEpisode <= 90) { // inseamna ca ma duc in episodul urmator
                    int indexEpisode = verifyNextEpisode(user.getAudioFile().getPodcastFile(), findTrack.getName());
                    if (indexEpisode == user.getAudioFile().getPodcastFile().getEpisodes().size() - 1) {// o sa am cazul in care este ultimul episod si il sterg
                        eraseHistory();
                        this.message = "Please load a source before skipping forward.";
                    } else { // inseamna ca este un episod oarecare si trebuie sa trec in episodul urmator de la inceput
                        user.setListeningTime(user.getListeningTime() + remainedTimeEpisode); // avansez in timp
                        this.message = "Skipped forward successfully.";
                    }
                } else  { // inseamna ca doar avansez in timp la episodul curent
                    user.setListeningTime(user.getListeningTime() + 90);
                    this.message = "Skipped forward successfully.";
                }
            } else {
                this.message = "The loaded source is not a podcast.";
            }
        } else {
            this.message = "Please load a source before attempting to forward.";
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
