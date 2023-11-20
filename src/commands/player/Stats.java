package commands.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.EpisodeInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

@JsonIgnoreProperties(value = { "username", "userHistory", "command", "timestamp" }) // pentru a ignora campurile mostenite
public class Stats extends CommandExecute {

    private String name = "";
    private int remainedTime = 0;
    private String repeat = "No Repeat";
    private boolean shuffle = false;
    private boolean paused = true;

    private static int sumEpisodes;
    private static int s;

    @JsonIgnoreProperties
    public Stats(Command command, LibraryInput library) {
        super(command, library);
    }

    public String getName() {
        return name;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public boolean isPaused() {
        return paused;
    }

    private SongInput listeningSong(Playlist playlist, int listeningTime) { // vreau sa fac o functie pentru a vedea la ce melodie a ajuns
        if (playlist.getListSongs() != null) {
            s = playlist.getListSongs().get(0).getDuration(); // fac un contor pentru a face suma melodiilor si o initializez cu durata primei melodii
            if (listeningTime < s)
                return playlist.getListSongs().get(0);
            for (int i = 1; i < playlist.getListSongs().size(); i++)
                if (listeningTime > s) // sa vad cand depaseste
                    s += playlist.getListSongs().get(i).getDuration();
                else
                    return playlist.getListSongs().get(i - 1);

            if (listeningTime <= s)
                return playlist.getListSongs().get(playlist.getListSongs().size() - 1);
        }
        return null;
    }

    private EpisodeInput verifyEpisodePodcast(PodcastInput podcast, int listeningTime){
        sumEpisodes = podcast.getEpisodes().get(0).getDuration(); // fac un contor pentru a face suma melodiilor si o initializez cu durata primei melodii
//        sumEpisodes = podcast.getEpisodes().get(0).getDuration(); // fac un contor pentru a face suma melodiilor si o initializez cu durata primei melodii
        if (listeningTime < sumEpisodes)
            return podcast.getEpisodes().get(0);
        for (int i = 1; i < podcast.getEpisodes().size(); i++)
            if (listeningTime > sumEpisodes) // sa vad cand depaseste
                sumEpisodes += podcast.getEpisodes().get(i).getDuration();
            else
                return podcast.getEpisodes().get(i - 1);

            if (listeningTime <= sumEpisodes)
                return podcast.getEpisodes().get(podcast.getEpisodes().size() - 1);
            return null;
    }

    public void execute() {
        // vreau sa vad user ul meu ce comenzi a fct pana acm
        UserHistory user = getUserHistory().get(verifyUser(getUsername())); // retin sa vad ce user am
        if (user.getAudioFile().getPodcast() != null) { // sa vad daca am retinut un podcast
            if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                if (user.isPlayPauseResult() == false) { // inseamnca ca e pe pauza si retin direct cat a ascultat
                    int listeningTime = user.getListeningTime();
                    EpisodeInput episodActual = verifyEpisodePodcast(user.getAudioFile().getPodcast(), listeningTime);

                    this.remainedTime = sumEpisodes - listeningTime;
                    this.name = episodActual.getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    int moreSeconds = getTimestamp() - user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int secondsNow = user.getListeningTime(); // sa vad cat timp a ascultat pana acm
                    EpisodeInput episodActual = verifyEpisodePodcast(user.getAudioFile().getPodcast(), moreSeconds + secondsNow);
                        this.remainedTime = sumEpisodes - (moreSeconds + secondsNow);
                        this.name = episodActual.getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                }
            } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = getTimestamp() - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                EpisodeInput episodActual = verifyEpisodePodcast(user.getAudioFile().getPodcast(), listeningTime);
                    this.remainedTime = sumEpisodes - listeningTime;
                    this.name = episodActual.getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
            }
        } else if (user.getAudioFile().getSong() != null) { // inseamna ca am incarcat un song
            // prima oara trebuie sa verific cat timp are song ul meu si cat timp a mai ramas din el
            if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                if (user.isPlayPauseResult() == false) {// inseamnca ca e pe pauza si retin direct cat a ascultat
                    int listeningTime = user.getListeningTime();
                    int timeSong = user.getAudioFile().getSong().getDuration();
                    if (timeSong - listeningTime >= 0) {// inseamna ca inca mai are de ascultat
                        this.remainedTime = timeSong - listeningTime;
                        this.name = user.getAudioFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    }
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    int lastPlay = user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int listeningTime = user.getListeningTime();
                    int timeSong = user.getAudioFile().getSong().getDuration();
                    if (timeSong - (listeningTime + (getTimestamp() - lastPlay)) >= 0) {// inse
                        this.remainedTime = timeSong - (listeningTime + (getTimestamp() - lastPlay));
                        this.name = user.getAudioFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    }
                }
            } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = getTimestamp() - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                int timeSong = user.getAudioFile().getSong().getDuration();
                if (timeSong - listeningTime >= 0) {// inseamna ca inca mai e timp se ascultat
                    this.remainedTime = timeSong - listeningTime;
                    this.name = user.getAudioFile().getSong().getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                }
            }
        } else if (user.getAudioFile().getPlaylist() != null) { // inseamna ca am un playlist si vreau sa vad la ce melodie am ajuns
            if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                if (user.isPlayPauseResult() == false) { // inseamnca ca e pe pauza si retin direct cat a ascultat
                    int listeningTime = user.getListeningTime();
                    SongInput song = listeningSong(user.getAudioFile().getPlaylist(), listeningTime);
                    if (song != null) {
                        this.remainedTime = s - listeningTime;
                        this.name = song.getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    }
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    int moreSeconds = getTimestamp() - user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int secondsNow = user.getListeningTime(); // sa vad cat timp a ascultat pana acm
                    SongInput song = listeningSong(user.getAudioFile().getPlaylist(), moreSeconds + secondsNow);
                    if (song != null) {
                        this.remainedTime = s - (moreSeconds + secondsNow);
                        this.name = song.getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    }
                }
            } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = getTimestamp() - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                SongInput song = listeningSong(user.getAudioFile().getPlaylist(), listeningTime);
                if (song != null) {
                    this.remainedTime = s - listeningTime;
                    this.name = song.getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                }
            }
        }
    }
}
