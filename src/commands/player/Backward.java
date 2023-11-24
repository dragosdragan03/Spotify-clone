package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class Backward extends CommandExecute {

    private String message;

    public Backward(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * muta utilizatorul cu 90 de secunde in urma daca a ascultat din episod mai mult de 90s
     * sau il muta la inceputul episodului daca a ascultat mai putin de 90s
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            if (user.getAudioFile().getPodcastFile() != null) { // este incarcat un podcast
                final int timeBackward = 90;
                FindTrack findTrack = new FindTrack(user, getTimestamp());
                findTrack.findTrackExecute(); // sa vad unde se alfa prin podcast
                // retin sa vad cat a mai ramas din episod
                int remainedTimeEpisode = findTrack.getRemainedTime();
                int listenedEpisode = findTrack.getEpisodeFound().getDuration()
                        - remainedTimeEpisode;
                if (listenedEpisode <= timeBackward) { // inseamna ca ma duc in episodul anterior
                    // userul se muta cu timpul inceputul episodului
                    user.setListeningTime(user.getListeningTime() - listenedEpisode);
                    this.message = "Rewound successfully.";
                } else { // inseamna ca doar avansez in timp la episodul curent
                    user.setListeningTime(user.getListeningTime() - timeBackward);
                    this.message = "Rewound successfully.";
                }
            } else {
               this.message = "The loaded source is not a podcast.";
            }
        } else  {
            this.message = "Please select a source before rewinding.";
        }
    }

    /**
     *
     * @return mesajul generat in metoda execute
     * daca a putut fi mutat inainte sau nu
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
