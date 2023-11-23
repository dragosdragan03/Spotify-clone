package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import main.Output;

public class Backward extends CommandExecute {

    private String message;

    public Backward(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            if (user.getAudioFile().getPodcastFile() != null) { // inseamna ca am incarcat un playlist
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute(); // sa vad unde se alfa prin podcast
                int remainedTimeEpisode = findTrack.getRemainedTime(); // retin sa vad cat a mai ramas din episod
                int listenedEpisode = findTrack.getEpisodeFound().getDuration() - remainedTimeEpisode;
                if (listenedEpisode <= 90) { // inseamna ca ma duc in episodul anterior
                    user.setListeningTime(user.getListeningTime() - listenedEpisode); // inseamna ca ma duc cu timpul in urma la inceputulepisodulu
                    this.message = "Rewound successfully.";
                } else { // inseamna ca doar avansez in timp la episodul curent
                    user.setListeningTime(user.getListeningTime() - 90);
                    this.message = "Rewound successfully.";
                }
            } else {
               this.message = "The loaded source is not a podcast.";
            }
        } else  {
            this.message = "Please select a source before rewinding.";
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
