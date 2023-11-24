package commands;

import fileio.input.PodcastInputModified;
import fileio.input.SongInput;

import java.util.ArrayList;

// vreau sa fac o clasa care sa mi retine toate comenzile care au fost date la un user
public class UserHistory {

    private String user;

    // retin sa vad ce podcsturi a ascultat pana acm si in cazul in care s a terminat il sterg
    private ArrayList<PodcastInputModified> pastPodcast = new ArrayList<>();
    private AudioFile audioFile; // retin in el ce mi a fost selectat
    private ArrayList<String> resultSearch = new ArrayList<>();

    // vreau sa fac un array list de melodii placute
    private ArrayList<SongInput> likedSongs = new ArrayList<>();
    /**
     * prima oara zic ca nu am incarcat nimic;
     * 1. ma folosesc sa vad cand a incarcat melodia;
     * 2. ma folosesc sa vad cand i a dat resume la AudioFile pentru a calcula timpul de ascultare
     */
    private int timeLoad = 0;
    private int listeningTime = 0; // il folosesc sa vad cat timp a ascultat din AudioFile
    private boolean playPauseResult = true; // cand ii dau load inseamnca ca e pe play (true)
    // o lista de playlist uri pentru un user
    private ArrayList<Playlist> userPlaylists = new ArrayList<>();

    private ArrayList<Playlist> followPlaylists = new ArrayList<>();

    /**
     * vreau sa creez un user cu numele user
     * @param user
     */
    public UserHistory(final String user) {
        this.user = user;
    }

    /**
     * @return numele userului
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @return tipul fisierului. Este o clasa si verific daca este podcast, playlist sau melodie
     */
    public AudioFile getAudioFile() {
        return audioFile;
    }

    /**
     * setez tipul fisierului (podcast, playlist sau melodie)
     * @param audioFile
     */
    public void setAudioFile(final AudioFile audioFile) {
        this.audioFile = audioFile;
    }

    /**
     *
     * @return un array list de nume de melodii gasite in search
     */
    public ArrayList<String> getResultSearch() {
        return resultSearch;
    }

    /**
     * folosesc acest setter pentru a putea sterge tot ce a fost cautat atunci
     * cand se da un select sau un search nou
     * @param resultSearch
     */
    public void setResultSearch(final ArrayList<String> resultSearch) {
        this.resultSearch = resultSearch;
    }

    /**
     *
     * @return numarul de secunde pe care l a ascultat userul
     */
    public int getListeningTime() {
        return listeningTime;
    }

    /**
     * ii dau update mereu la fiecare comanda
     * @param listeningTime cate sec a ascultat un user
     */
    public void setListeningTime(final int listeningTime) {
        this.listeningTime = listeningTime;
    }

    /**
     *
     * @return cand a fost incarcat un fisier sau ultimul moment de timp dat dupa laod
     */
    public int getTimeLoad() {
        return timeLoad;
    }

    /**
     * il setez atunci cand ii dau load
     * @param timeLoad momentul de timp cand a fost incarcata o melodie/ sau o noua comanda
     */
    public void setTimeLoad(final int timeLoad) {
        this.timeLoad = timeLoad;
    }

    /**
     *
     * @return daca userul este pe play sau pe pauza indiferent de ce fisier asculta
     */
    public boolean isPlayPauseResult() {
        return playPauseResult;
    }

    /**
     *
     * @param playPauseResult il modific in functia playPause
     */
    public void setPlayPauseResult(final boolean playPauseResult) {
        this.playPauseResult = playPauseResult;
    }

    /**
     *
     * @return lista cu playlisturi a userului
     */
    public ArrayList<Playlist> getUserPlaylists() {
        return userPlaylists;
    }

    /**
     *
     * @return lista de melodii placute a userului
     */
    public ArrayList<SongInput> getLikedSongs() {
        return likedSongs;
    }

    /**
     *
     * @return lista de podcasturi pe care le a ascultat userul pana acm
     */
    public ArrayList<PodcastInputModified> getPastPodcast() {
        return pastPodcast;
    }

    /**
     *
     * @return lista de playlisturi la care a dat follow userul
     */
    public ArrayList<Playlist> getFollowPlaylists() {
        return followPlaylists;
    }
}
