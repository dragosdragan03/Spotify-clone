package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.PodcastInputModified;
import fileio.input.SongInputModified;
import main.Output;

public class Prev extends CommandExecute {

    private String message;

    public Prev(Command command, LibraryInput library) {
        super(command, library);
    }

    private int prevSong(Playlist playlist, String songName) {
        for (int i = 0; i < playlist.getListSongs().size(); i++) // vreau sa fac un for sa vad daca exista o melodie inainte
            if (playlist.getListSongs().get(i).getSong().getName() == songName)
                return i;
        return -1;
    }

    private int prevEpisode(PodcastInput podcast, String episodeName) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++)
            if (podcast.getEpisodes().get(i).getName().equals(episodeName))
                return i;

        return -1;
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            FindTrack findTrack = new FindTrack(user, getTimestamp());
            findTrack.findTrackExecute();
            if (user.getAudioFile().getPlaylistFile() != null) {
                int durationSong = findTrack.getSongFound().getSong().getDuration(); // durata melodiei la care am ramas
                int timeListenedSong = durationSong - findTrack.getRemainedTime(); // sa verific cat am ascultat
                user.setPlayPauseResult(true);
                if (timeListenedSong != 0) {
                    user.setListeningTime(user.getListeningTime() - timeListenedSong);
                    this.message = "Returned to previous track successfully. The current track is " + findTrack.getName() + ".";
                } else { // inseamna ca e la secunda 0 deci trebuie sa ma duc in melodie anterioara
                    int indexPrevSong = prevSong(user.getAudioFile().getPlaylistFile(), findTrack.getName());
                    if (indexPrevSong == 0) { // inseamna ca e prima melodie
                        this.message = "Returned to previous track successfully. The current track is " + findTrack.getName() + ".";
                    } else if (indexPrevSong > 0) { // inseamna ca nu e prima melodie
                        int listeningTime = user.getListeningTime();
                        int durationPrevSong = user.getAudioFile().getPlaylistFile().getListSongs().get(indexPrevSong - 1).getSong().getDuration();
                        user.setListeningTime(listeningTime - durationPrevSong);
                        String namePrevSong = user.getAudioFile().getPlaylistFile().getListSongs().get(indexPrevSong - 1).getSong().getName();
                        this.message = "Returned to previous track successfully. The current track is " + namePrevSong + ".";
                    }
                }
            } else if (user.getAudioFile().getSongFile() != null) {
                // am trei cazuri de tratat
                int durationSong = user.getAudioFile().getSongFile().getSong().getDuration();
                int timeListenedSong = durationSong - findTrack.getRemainedTime();
                if (timeListenedSong != 0) {// inseamna ca a trecut de prima secunda
                    user.setListeningTime(user.getListeningTime() - timeListenedSong);
                    this.message = "Returned to previous track successfully. The current track is " + user.getAudioFile().getSongFile().getSong().getName() + ".";
                } else {
                    this.message = "AM DAT IN SPATE LA O MELODIE";
                    eraseHistory();
                }
            } else if (user.getAudioFile().getPodcastFile() != null) {
                int durationEpisod = findTrack.getEpisodeFound().getDuration(); // durata melodiei la care am ramas
                int timeListenedSong = durationEpisod - findTrack.getRemainedTime(); // sa verific cat am ascultat
                if (timeListenedSong != 0) { // inseamna ca a ascultat deja ceva din episodul din podcast
                    user.setListeningTime(user.getListeningTime() - timeListenedSong);
                    this.message = "Returned to previous track successfully. The current track is " + findTrack.getName() + ".";
                } else { // inseamna ca e la secunda 0 deci trebuie sa ma duc in melodie anterioara
                    int indexPrevEpisode = prevEpisode(user.getAudioFile().getPodcastFile(), findTrack.getName());
                    if (indexPrevEpisode == 0) { // inseamna ca e primul episod
                        this.message = "Returned to previous track successfully. The current track is " + findTrack.getName() + ".";
                    } else if (indexPrevEpisode > 0) { // inseamna ca nu e prima melodie
                        int listeningTime = user.getListeningTime();
                        int durationPrevEpisode = user.getAudioFile().getPodcastFile().getEpisodes().get(indexPrevEpisode - 1).getDuration();
                        user.setListeningTime(listeningTime - durationPrevEpisode);
                        String namePrevEpisode = user.getAudioFile().getPodcastFile().getEpisodes().get(indexPrevEpisode - 1).getName();
                        this.message = "Returned to previous track successfully. The current track is " + namePrevEpisode + ".";
                    }
                }
            }
        } else {
            this.message = "Please load a source before returning to the previous track.";
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
