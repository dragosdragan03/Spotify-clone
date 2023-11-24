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

    public FindTrack(final UserHistory user, final int timeStamp) {
        this.user = user;
        this.timeStamp = timeStamp;
    }

    /**
     * @return numele AudioFile ului pe care il asculta userul
     */
    public String getName() {
        return name;
    }

    /**
     * @return daca userul este pe play sau pause
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return cat timp a mai ramas de ascultat din episod/melodie
     */
    public int getRemainedTime() {
        return remainedTime;
    }

    /**
     * @return melodia pe care o asculta userul (din playlist sau Song)
     */
    public SongInputModified getSongFound() {
        return songFound;
    }

    /**
     * @return daca userul este sau nu pe repeat si returneaza 0/1/2 in functie de ce a ales
     */
    public int getRepeat() {
        return repeat;
    }

    /**
     * @return episodul la care a ajuns userul
     */
    public EpisodeInput getEpisodeFound() {
        return episodeFound;
    }

    /**
     * @return daca userul asculta sau nu playlistul pe shuffle
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     * @param playlist      plylistul in care caut
     * @param listeningTime cat timp a ascultat userul
     * @return melodia pe care o asculta din playlist
     */
    public SongInputModified listeningSong(final Playlist playlist, final int listeningTime) {
        if (playlist.getListSongs() != null) {
            // fac un contor pentru a face suma melodiilor
            sumSongs = playlist.getListSongs().get(0).getSong().getDuration();
            if (listeningTime < sumSongs) {
                return playlist.getListSongs().get(0);
            }
            for (int i = 1; i < playlist.getListSongs().size(); i++) {
                if (listeningTime >= sumSongs) { // sa vad cand depaseste
                    sumSongs += playlist.getListSongs().get(i).getSong().getDuration();
                } else {
                    return playlist.getListSongs().get(i - 1);
                }
            }
            if (listeningTime <= sumSongs) {
                return playlist.getListSongs().get(playlist.getListSongs().size() - 1);
            }
        }
        return null;
    }

    /**
     * @param podcast       podcastul pe care il asculta userul
     * @param listeningTime cat timp a ascultat din podcast
     * @return episodul pe care il asculta
     */
    private EpisodeInput verifyEpisodePodcast(final PodcastInput podcast, final int listeningTime) {
        // fac un contor pentru a face suma episoadelor
        sumEpisodes = podcast.getEpisodes().get(0).getDuration();
        if (listeningTime < sumEpisodes) {
            return podcast.getEpisodes().get(0);
        }
        for (int i = 1; i < podcast.getEpisodes().size(); i++) {
            if (listeningTime > sumEpisodes) { // sa vad cand depaseste
                sumEpisodes += podcast.getEpisodes().get(i).getDuration();
            } else {
                return podcast.getEpisodes().get(i - 1);
            }
        }
        if (listeningTime <= sumEpisodes) {
            return podcast.getEpisodes().get(podcast.getEpisodes().size() - 1);
        }
        return null;
    }

    /*fac o metoda pentru a mi retuna melodia care se repeta la nesfarsit*/
    private SongInputModified returnRepeatInfiniteSong(final Playlist playlist) {
        for (SongInputModified song : playlist.getListSongs()) {
            if (song.getRepeat() == 2) { // sa vad care song din playlist se repeta la nesfarsit
                return song;
            }
        }
        return null;
    }

    private void repeatPlaylist2() {
        // fac suma pana la melodia asta
        // scad din timpul trecut suma asta (user.getLiseningtime
        this.shuffle = user.getAudioFile().getPlaylistFile().isShuffle();
        SongInputModified infiniteSong =
                returnRepeatInfiniteSong(user.getAudioFile().getPlaylistFile());
        if (infiniteSong != null) {
            int durationSong = infiniteSong.getSong().getDuration();

            int sumSong = 0; // o fac in gol pentru a vedea suma melodiilor
            for (SongInputModified iter : user.getAudioFile().getPlaylistFile().getListSongs()) {
                if (iter == infiniteSong) {
                    break;
                } else {
                    sumSong += iter.getSong().getDuration();
                }
            }

            // vreau sa scad ce a fost inainte de melodia mea pusa in bucla
            user.setListeningTime(user.getListeningTime() - sumSong);

            // vreau sa vad cat a ascultat efectiv din melodia mea
            while (user.getListeningTime() >= durationSong) {
                user.setListeningTime(user.getListeningTime() - durationSong);
            }
            int listenedSong = user.getListeningTime();
            // setez timpul adevarat cat a ascultat din playlist
            user.setListeningTime(user.getListeningTime() + sumSong);

            this.remainedTime = durationSong - listenedSong;
            this.songFound = infiniteSong;
            this.name = infiniteSong.getSong().getName();
            this.paused = !user.isPlayPauseResult();
            this.repeat = 2;
            // trebuie sa i dau update la timelistening
        }
    }

    private void findEpisodePodcast() {
        // daca am podcast retin repeat ul direct in audio file
        this.repeat = user.getAudioFile().getRepeat();
        // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
        if (user.getListeningTime() != 0) {
            // inseamnca ca e pe pauza si retin direct cat a ascultat
            if (!user.isPlayPauseResult()) {
                int listeningTime = user.getListeningTime();
                EpisodeInput episodActual =
                        verifyEpisodePodcast(user.getAudioFile().getPodcastFile(), listeningTime);
                this.episodeFound = episodActual;
                this.remainedTime = sumEpisodes - listeningTime;
                this.name = episodActual.getName();
                this.paused = !user.isPlayPauseResult(); //NU este pe play
            } else { // inseamna ca e pe play si au mai fost date Play/Pause
                // ultima oara cand a fost dat play (inseamna ca inca asculta)
                int moreSeconds = timeStamp - user.getTimeLoad();
                int secondsNow = user.getListeningTime(); // sa vad cat timp a ascultat pana acm
                EpisodeInput episodActual =
                        verifyEpisodePodcast(user.getAudioFile().getPodcastFile(),
                                moreSeconds + secondsNow);
                this.episodeFound = episodActual;
                this.remainedTime = sumEpisodes - (moreSeconds + secondsNow);
                this.name = episodActual.getName();
                this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
            }
        } else { // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
            // retin sa vad cand a fost incarcata melodia
            int listeningTime = timeStamp - user.getTimeLoad();
            EpisodeInput episodActual =
                    verifyEpisodePodcast(user.getAudioFile().getPodcastFile(), listeningTime);
            this.episodeFound = episodActual;
            this.remainedTime = sumEpisodes - listeningTime;
            this.name = episodActual.getName();
            this.paused = !user.isPlayPauseResult(); // retin cu not, deoarece NU este pe play;
        }
    }

    private void findSong() {
        this.repeat = user.getAudioFile().getSongFile().getRepeat();
        // prima oara trebuie sa verific cat timp are song ul meu si cat timp a mai ramas din el
        if (user.getAudioFile().getSongFile().getRepeat() == 0
                || user.getListeningTime()
                < user.getAudioFile().getSongFile().getSong().getDuration()) {
            // inseamna ca a fost un play/pause pana acm si sa vad cat timp a ascultat
            if (user.getListeningTime() != 0) {
                // inseamnca ca e pe pauza si retin direct cat a ascultat
                if (!user.isPlayPauseResult()) {
                    int listeningTime = user.getListeningTime();
                    int timeSong = user.getAudioFile().getSongFile().getSong().getDuration();
                    if (timeSong - listeningTime >= 0) { // verific daca mai are de ascultat
                        this.songFound = user.getAudioFile().getSongFile();
                        this.remainedTime = timeSong - listeningTime;
                        this.name = user.getAudioFile().getSongFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); //NU este pe play;
                        this.repeat = user.getAudioFile().getSongFile().getRepeat();
                    }
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int lastPlay = user.getTimeLoad();
                    int listeningTime = user.getListeningTime();
                    int timeSong = user.getAudioFile().getSongFile().getSong().getDuration();
                    if (timeSong - (listeningTime + (timeStamp - lastPlay)) >= 0) {
                        this.songFound = user.getAudioFile().getSongFile();
                        this.remainedTime =
                                timeSong - (listeningTime + (timeStamp - lastPlay));
                        this.name = user.getAudioFile().getSongFile().getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // NU este pe play;
                        this.repeat = user.getAudioFile().getSongFile().getRepeat();
                    }
                }
            } else {
                // inseamna ca nu a dat niciun Play/Pause pana acm deci a ascultat incotninuu
                int listeningTime = timeStamp - user.getTimeLoad();
                int timeSong = user.getAudioFile().getSongFile().getSong().getDuration();
                // inseamna ca inca mai e timp se ascultat
                if (timeSong - listeningTime >= 0) {
                    this.songFound = user.getAudioFile().getSongFile();
                    this.remainedTime = timeSong - listeningTime;
                    this.name = user.getAudioFile().getSongFile().getSong().getName();
                    this.paused = !user.isPlayPauseResult(); // NU este pe play;
                    this.repeat = user.getAudioFile().getSongFile().getRepeat();
                }
            }
        } else if (user.getAudioFile().getSongFile().getRepeat() == 1) {
            // verific daca a depasit durata unei melodii
            if (user.getListeningTime()
                    > user.getAudioFile().getSongFile().getSong().getDuration()) {
                // inseamna ca am intrat in urmatorea melodie
                if (user.getListeningTime()
                        < 2 * user.getAudioFile().getSongFile().getSong().getDuration()) {
                    user.getAudioFile().getSongFile().setRepeat(0);
                    this.remainedTime =
                            2 * user.getAudioFile().getSongFile().getSong().getDuration()
                                    - user.getListeningTime();
                    this.songFound = user.getAudioFile().getSongFile();
                    this.name = user.getAudioFile().getSongFile().getSong().getName();
                    this.paused = !user.isPlayPauseResult();
                    this.repeat = user.getAudioFile().getSongFile().getRepeat();
                    user.setListeningTime(user.getListeningTime()
                            - this.songFound.getSong().getDuration());
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
    }

    /**
     * aceasta metoda este un fel de Stats. Imi returneaza toate detaliile despre userul meu.
     * cat timp a ascultat; melodia, episodul la care a ajuns; cat a ramas din track;
     * daca este pe repeat/shuffle/pauza
     */
    public void findTrackExecute() {
        if (user.getAudioFile().getPodcastFile() != null) { // sa vad daca am retinut un podcast
            findEpisodePodcast();
        } else if (user.getAudioFile().getSongFile() != null) { // inseamna ca am incarcat un song
            findSong();
            // inseamna ca am un playlist si vreau sa vad la ce melodie am ajuns
        } else if (user.getAudioFile().getPlaylistFile() != null
                && user.getAudioFile().getPlaylistFile().getRepeatPlaylist() != 2) {
            this.shuffle = user.getAudioFile().getPlaylistFile().isShuffle();
            if (user.getListeningTime() != 0) {
                if (!user.isPlayPauseResult()) {
                    int listeningTime = user.getListeningTime();
                    SongInputModified song =
                            listeningSong(user.getAudioFile().getPlaylistFile(), listeningTime);
                    if (song != null) {
                        this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                        this.songFound = song;
                        this.remainedTime = sumSongs - listeningTime;
                        this.name = song.getSong().getName();
                        this.paused = !user.isPlayPauseResult(); // NU este pe play;
                        // inseamna ca mi a trecut de parcurs o data playlist ul
                    } else if (user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) {
                        user.setListeningTime(user.getListeningTime() - sumSongs);
                        SongInputModified songInRepeatPlaylist =
                                listeningSong(user.getAudioFile().getPlaylistFile(),
                                        user.getListeningTime());
                        if (songInRepeatPlaylist != null) {
                            this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                            this.songFound = songInRepeatPlaylist;
                            this.remainedTime = sumSongs - user.getListeningTime();
                            this.name = songInRepeatPlaylist.getSong().getName();
                            this.paused = !user.isPlayPauseResult();
                        }
                    }
                } else { // inseamna ca e pe play si au mai fost date Play/Pause
                    // ultima oara cand a fost dat play (inseamna ca inca asculta)
                    int moreSeconds = timeStamp - user.getTimeLoad();
                    int secondsNow = user.getListeningTime(); // sa vad cat timp a ascultat
                    SongInputModified song = listeningSong(user.getAudioFile().getPlaylistFile(),
                            moreSeconds + secondsNow);
                    if (song != null) {
                        this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                        this.songFound = song;
                        this.remainedTime = sumSongs - (moreSeconds + secondsNow);
                        this.name = song.getSong().getName();
                        this.paused = !user.isPlayPauseResult();
                    } else if (user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) {
                        user.setListeningTime(user.getListeningTime() - sumSongs);
                        SongInputModified songInRepeatPlaylist =
                                listeningSong(user.getAudioFile().getPlaylistFile(),
                                        user.getListeningTime());
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
                int listeningTime = timeStamp - user.getTimeLoad();
                SongInputModified song =
                        listeningSong(user.getAudioFile().getPlaylistFile(), listeningTime);
                if (song != null) {
                    this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                    this.songFound = song;
                    this.remainedTime = sumSongs - listeningTime;
                    this.name = song.getSong().getName();
                    this.paused = !user.isPlayPauseResult();
                } else if (user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 1) {
                    user.setListeningTime(user.getListeningTime() - sumSongs);
                    SongInputModified songInRepeatPlaylist =
                            listeningSong(user.getAudioFile().getPlaylistFile(),
                                    user.getListeningTime());
                    if (songInRepeatPlaylist != null) {
                        this.repeat = user.getAudioFile().getPlaylistFile().getRepeatPlaylist();
                        this.songFound = songInRepeatPlaylist;
                        this.remainedTime = sumSongs - user.getListeningTime();
                        this.name = songInRepeatPlaylist.getSong().getName();
                        this.paused = !user.isPlayPauseResult();
                    }
                }
            }
        } else if (user.getAudioFile().getPlaylistFile() != null
                && user.getAudioFile().getPlaylistFile().getRepeatPlaylist() == 2) {
            repeatPlaylist2();
        }
    }
}
