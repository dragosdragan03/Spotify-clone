package commands.player;

import commands.Playlist;
import commands.UserHistory;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInputModified;

public class FindTrack {

    private String name = ""; // numele trackului pe care il ascult acm
    private int timeStamp;
    private UserHistory user;
    private SongInputModified songFound;
    private EpisodeInput episodeFound;
    private boolean paused = true;
    private int repeat = 0;
    private int remainedTime = 0;
    private int sumEpisodes;
    private int sumSongs;
    private boolean shuffle;

    public FindTrack(UserHistory user, int timeStamp) {
        this.user = user;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public SongInputModified getSongFound() {
        return songFound;
    }

    public int getRepeat() {
        return repeat;
    }

    public EpisodeInput getEpisodeFound() {
        return episodeFound;
    }

    public int getSumSongs() {
        return sumSongs;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public SongInputModified listeningSong(Playlist playlist, int listeningTime) { // vreau sa fac o functie pentru a vedea la ce melodie a ajuns
        if (playlist.getListSongs() != null) {
            sumSongs = playlist.getListSongs().get(0).getSong().getDuration(); // fac un contor pentru a face suma melodiilor si o initializez cu durata primei melodii
            if (listeningTime < sumSongs)
                return playlist.getListSongs().get(0);
            for (int i = 1; i < playlist.getListSongs().size(); i++)
                if (listeningTime >= sumSongs) // sa vad cand depaseste
                    sumSongs += playlist.getListSongs().get(i).getSong().getDuration();
                else
                    return playlist.getListSongs().get(i - 1);

            if (listeningTime <= sumSongs)
                return playlist.getListSongs().get(playlist.getListSongs().size() - 1);
        }
        return null;
    }

    private EpisodeInput verifyEpisodePodcast(PodcastInput podcast, int listeningTime) {
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

    /*fac o metoda pentru a mi retuna melodia care se repeta la nesfarsit*/
    private SongInputModified returnRepeatInfiniteSong(Playlist playlist) {
        for (SongInputModified song : playlist.getListSongs())
            if (song.getRepeat() == 2) // sa vad care song din playlist se repeta la nesfarsit
                return song;
        return null;
    }

    public void findTrackExecute() {
        if (user.getAudioFile().getPodcastFile() != null) { // sa vad daca am retinut un podcast
            this.repeat = user.getAudioFile().getRepeat(); // daca am podcst retin repeat ul direct in audio file
            if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                if (user.isPlayPauseResult() == false) { // inseamnca ca e pe pauza si retin direct cat a ascultat
                    int listeningTime = user.getListeningTime();
                    EpisodeInput episodActual = verifyEpisodePodcast(user.getAudioFile().getPodcastFile(), listeningTime);
                    this.episodeFound = episodActual;
                    this.remainedTime = sumEpisodes - listeningTime;
                    this.name = episodActual.getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    int moreSeconds = timeStamp - user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int secondsNow = user.getListeningTime(); // sa vad cat timp a ascultat pana acm
                    EpisodeInput episodActual = verifyEpisodePodcast(user.getAudioFile().getPodcastFile(), moreSeconds + secondsNow);
                    this.episodeFound = episodActual;
                    this.remainedTime = sumEpisodes - (moreSeconds + secondsNow);
                    this.name = episodActual.getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                }
            } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = timeStamp - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                EpisodeInput episodActual = verifyEpisodePodcast(user.getAudioFile().getPodcastFile(), listeningTime);
                this.episodeFound = episodActual;
                this.remainedTime = sumEpisodes - listeningTime;
                this.name = episodActual.getName();
                this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
            }
        } else if (user.getAudioFile().getSongFile() != null) { // inseamna ca am incarcat un song
            this.repeat = user.getAudioFile().getSongFile().getRepeat();
            // prima oara trebuie sa verific cat timp are song ul meu si cat timp a mai ramas din el
            if (user.getAudioFile().getSongFile().getRepeat() == 0 || user.getListeningTime() < user.getAudioFile().getSongFile().getSong().getDuration()) {
                if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                    if (user.isPlayPauseResult() == false) {// inseamnca ca e pe pauza si retin direct cat a ascultat
                        int listeningTime = user.getListeningTime();
                        int timeSong = user.getAudioFile().getSongFile().getSong().getDuration();
                        if (timeSong - listeningTime >= 0) {// inseamna ca inca mai are de ascultat
                            this.songFound = user.getAudioFile().getSongFile();
                            this.remainedTime = timeSong - listeningTime;
                            this.name = user.getAudioFile().getSongFile().getSong().getName();
                            this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                            this.repeat = user.getAudioFile().getSongFile().getRepeat();
                        }
                    } else { // inseamna ca e pe play si au mai fost date Play/Pause
                        int lastPlay = user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                        int listeningTime = user.getListeningTime();
                        int timeSong = user.getAudioFile().getSongFile().getSong().getDuration();
                        if (timeSong - (listeningTime + (timeStamp - lastPlay)) >= 0) {// inse
                            this.songFound = user.getAudioFile().getSongFile();
                            this.remainedTime = timeSong - (listeningTime + (timeStamp - lastPlay));
                            this.name = user.getAudioFile().getSongFile().getSong().getName();
                            this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                            this.repeat = user.getAudioFile().getSongFile().getRepeat();
                        }
                    }
                } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                    int listeningTime = timeStamp - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                    int timeSong = user.getAudioFile().getSongFile().getSong().getDuration();
                    if (timeSong - listeningTime >= 0) {// inseamna ca inca mai e timp se ascultat
                        this.songFound = user.getAudioFile().getSongFile();
                        this.remainedTime = timeSong - listeningTime;
                        this.name = user.getAudioFile().getSongFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                        this.repeat = user.getAudioFile().getSongFile().getRepeat();
                    }
                }
            } else if (user.getAudioFile().getSongFile().getRepeat() == 1) { // trebuie sa vad cat am ascultat
                // trebuie sa vad cat am ascultat si sa vad cat a mai ramas din melodie
                if (user.getListeningTime() > user.getAudioFile().getSongFile().getSong().getDuration()) { // inseamna ca am depasit durata unei melodii
                    if (user.getListeningTime() < 2 * user.getAudioFile().getSongFile().getSong().getDuration()) { // inseamna ca am intrat in urmatorea melodie
                        user.getAudioFile().getSongFile().setRepeat(0);
                        this.remainedTime = 2 * user.getAudioFile().getSongFile().getSong().getDuration() - user.getListeningTime();
                        this.songFound = user.getAudioFile().getSongFile();
                        this.name = user.getAudioFile().getSongFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult();
                        this.repeat = user.getAudioFile().getSongFile().getRepeat();
                        user.setListeningTime(user.getListeningTime() - user.getAudioFile().getSongFile().getSong().getDuration());
                    }
                }
            } else if (user.getAudioFile().getSongFile().getRepeat() == 2) {
                int durationSong = user.getAudioFile().getSongFile().getSong().getDuration();
                this.remainedTime = durationSong - user.getListeningTime() % durationSong;
                this.songFound = user.getAudioFile().getSongFile();
                this.name = user.getAudioFile().getSongFile().getSong().getName();
                this.paused = !user.isPlayPauseResult();
                this.repeat = user.getAudioFile().getSongFile().getRepeat();

                while (user.getListeningTime() > durationSong) {
                    user.setListeningTime(user.getListeningTime() - durationSong);
                }
            }
        } else if (user.getAudioFile().getPlaylistFile() != null && user.getAudioFile().getPlaylistFile().getRepeatPlaylist() != 2) { // inseamna ca am un playlist si vreau sa vad la ce melodie am ajuns
            this.shuffle = user.getAudioFile().getPlaylistFile().isShuffle();
            if (user.getListeningTime() != 0) { // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
                if (user.isPlayPauseResult() == false) { // inseamnca ca e pe pauza si retin direct cat a ascultat
                    int listeningTime = user.getListeningTime();
                    SongInputModified song = listeningSong(user.getAudioFile().getPlaylistFile(), listeningTime);
                    if (song != null) {
                        this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                        this.songFound = song;
                        this.remainedTime = sumSongs - listeningTime;
                        this.name = song.getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    } else if (user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) { // inseamna ca mi a trecut de parcurs o data playlist ul
                        user.setListeningTime(user.getListeningTime() - sumSongs);
                        SongInputModified songInRepeatPlaylist = listeningSong(user.getAudioFile().getPlaylistFile(), user.getListeningTime());
                        if (songInRepeatPlaylist != null) {
                            this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                            this.songFound = songInRepeatPlaylist;
                            this.remainedTime = sumSongs - user.getListeningTime();
                            this.name = songInRepeatPlaylist.getSong().getName();
                            this.paused = !user.isPlayPauseResult();
                        }
                    }
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    int moreSeconds = timeStamp - user.getTimeLoad(); // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int secondsNow = user.getListeningTime(); // sa vad cat timp a ascultat pana acm
                    SongInputModified song = listeningSong(user.getAudioFile().getPlaylistFile(), moreSeconds + secondsNow);
                    if (song != null) {
                        this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                        this.songFound = song;
                        this.remainedTime = sumSongs - (moreSeconds + secondsNow);
                        this.name = song.getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                    } else if (user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) {
                        user.setListeningTime(user.getListeningTime() - sumSongs);
                        SongInputModified songInRepeatPlaylist = listeningSong(user.getAudioFile().getPlaylistFile(), user.getListeningTime());
                        if (songInRepeatPlaylist != null) {
                            this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                            this.songFound = songInRepeatPlaylist;
                            this.remainedTime = sumSongs - user.getListeningTime();
                            this.name = songInRepeatPlaylist.getSong().getName();
                            this.paused = !user.isPlayPauseResult();
                        }
                    }
                }
            } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = timeStamp - user.getTimeLoad(); // retin sa vad cand a fost incarcata melodia (timpul 0)
                SongInputModified song = listeningSong(user.getAudioFile().getPlaylistFile(), listeningTime);
                if (song != null) {
                    this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                    this.songFound = song;
                    this.remainedTime = sumSongs - listeningTime;
                    this.name = song.getSong().getName();
                    this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
                } else if (user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) {
                    user.setListeningTime(user.getListeningTime() - sumSongs);
                    SongInputModified songInRepeatPlaylist = listeningSong(user.getAudioFile().getPlaylistFile(), user.getListeningTime());
                    if (songInRepeatPlaylist != null) {
                        this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                        this.songFound = songInRepeatPlaylist;
                        this.remainedTime = sumSongs - user.getListeningTime();
                        this.name = songInRepeatPlaylist.getSong().getName();
                        this.paused = !user.isPlayPauseResult();
                    }
                }
            }
        } else if (user.getAudioFile().getPlaylistFile() != null && user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 2) {
            // fac suma pana la melodia asta
            // scad din timpul trecut suma asta (user.getLiseningtime
            this.shuffle = user.getAudioFile().getPlaylistFile().isShuffle();
            SongInputModified infiniteSong = returnRepeatInfiniteSong(user.getAudioFile().getPlaylistFile());
            if (infiniteSong != null) {
                int durationSong = infiniteSong.getSong().getDuration();

                int sumSong = 0;// o fac in gol pentru a vedea suma melodiilor
                for (SongInputModified iter : user.getAudioFile().getPlaylistFile().getListSongs())
                    if (iter == infiniteSong)
                        break;
                    else
                        sumSong += iter.getSong().getDuration();

                user.setListeningTime(user.getListeningTime() - sumSong); // vreau sa scad ce a fost inainte de melodia mea pusa in bucla

                while (user.getListeningTime() >= durationSong) { // vreau sa vad cat a ascultat efectiv din melodia mea
                    user.setListeningTime(user.getListeningTime() - durationSong);
                }
                int listenedSong = user.getListeningTime();
                user.setListeningTime(user.getListeningTime() + sumSong); // setez listening timpul adevarat

                this.remainedTime = durationSong - listenedSong;
                this.songFound = infiniteSong;
                this.name = infiniteSong.getSong().getName();
                this.paused = !user.isPlayPauseResult();
                this.repeat = 2;
                // trebuie sa i dau update la timelistening

            }
        }
    }
}
