package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.PodcastInputModified;
import main.Output;

public class Load extends CommandExecute {

    private String message;
    public Load(Command command, LibraryInput library) {
        super(command, library);
    }

    /*fac o metoda pentru a vedea daca a mai fost incarcat podcastul asta*/
    private int searchPodcast(UserHistory user) {
        if (user.getPastPodcast().size() != 0) { // deci asta inseamna ca a mai ascultat podcasturi
            PodcastInput podcast = user.getAudioFile().getPodcast(); // retin podcastul pe care l am incarcat
            for (int i = 0; i < user.getPastPodcast().size(); i++)
                if (podcast.getName() == user.getPastPodcast().get(i).getName()) {// verific sa vad daca a mai fost ascultat podcastul
                    user.setTimeLoad(getTimestamp());
                    user.setPlayPauseResult(true);
                    user.setListeningTime(user.getPastPodcast().get(i).getListeningTimePodcast());
                    return 1; // inseamna ca s a gasit
                }
        }
        return -1; // inseamna ca n am gasit niciun podcast
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getAudioFile() != null && user.getTimeLoad() == 0 && user.getResultSearch().size() == 0) { // asta inseamna ca s a selectat ceva deja
            if (user.getAudioFile().getPlaylist() != null && user.getAudioFile().getPlaylist().getListSongs().size() != 0) {
                this.message = "Playback loaded successfully."; // retin mesajul si l afisez
                // chiar daca am fct load ul vreau sa pastrez valoarea selectata ca sa o pot prelucra
                user.setTimeLoad(getTimestamp()); // retin cand a fost incarcat fisierul
                user.setPlayPauseResult(true); // inseamna ca i am dat play
                return;
            }
            if (user.getAudioFile().getPlaylist() != null && user.getAudioFile().getPlaylist().getListSongs().size() == 0) {
                this.message = "You can't load an empty audio collection!";
                return;
            }
            if (user.getAudioFile().getPodcast() != null && searchPodcast(user) == 1) { // inseamnca ca am selectat un podcast si a mai fost ascultat pana acum
                this.message = "Playback loaded successfully.";
                return;
            }
            this.message = "Playback loaded successfully."; // retin mesajul si l afisez
            // chiar daca am fct load ul vreau sa pastrez valoarea selectata ca sa o pot prelucra
            user.setTimeLoad(getTimestamp()); // retin cand a fost incarcat fisierul
            user.setPlayPauseResult(true); // inseamna ca i am dat play
        }
        else
        {
            this.message = "Please select a source before attempting to load.";
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
