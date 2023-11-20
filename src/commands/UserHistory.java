package commands;

import fileio.input.PodcastInput;
import fileio.input.PodcastInputModified;
import fileio.input.SongInput;

import java.util.ArrayList;

// vreau sa fac o clasa care sa mi retine toate comenzile care au fost date la un user
public class UserHistory{

    private String user;
    private ArrayList<PodcastInputModified> pastPodcast = new ArrayList<>(); // retin sa vad ce podcsturi a ascultat pana acm si in cazul in care s a terminat il sterg
    private AudioFile audioFile; // retin in el ce mi a fost selectat
    private ArrayList<String> resultSearch = new ArrayList<>(); // fac un arraylist pentru toate comenzile unui user
    private ArrayList<SongInput> likedSongs = new ArrayList<>(); // vreau sa fac un array list de melodii like
    /**
     * prima oara zic ca nu am incarcat nimic;
     * 1. ma folosesc sa vad cand a incarcat melodia;
     * 2. ma folosesc sa vad cand i a dat resume la AudioFile pentru a calcula timpul de ascultare
     */
    private int timeLoad = 0;
    private int listeningTime = 0; // il folosesc sa vad cat timp a ascultat din AudioFile
    private boolean playPauseResult = true; // cand ii dau load inseamnca ca e pe play (1), altfel pe nimic

    private ArrayList<Playlist> userPlaylists = new ArrayList<>(); // o lista de playlist uri pentru un user


    public UserHistory(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public AudioFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(AudioFile audioFile) {
        this.audioFile = audioFile;
    }

    public ArrayList<String> getResultSearch() {
        return resultSearch;
    }

    public void setResultSearch(ArrayList<String> resultSearch) {
        this.resultSearch = resultSearch;
    }

    public int getListeningTime() {
        return listeningTime;
    }

    public void setListeningTime(int listeningTime) {
        this.listeningTime = listeningTime;
    }

    public int getTimeLoad() {
        return timeLoad;
    }

    public void setTimeLoad(int timeLoad) {
        this.timeLoad = timeLoad;
    }

    public boolean isPlayPauseResult() {
        return playPauseResult;
    }

    public void setPlayPauseResult(boolean playPauseResult) {
        this.playPauseResult = playPauseResult;
    }

    public ArrayList<Playlist> getUserPlaylists() {
        return userPlaylists;
    }

    public ArrayList<SongInput> getLikedSongs() {
        return likedSongs;
    }

    public void setPastPodcast(ArrayList<PodcastInputModified> pastPodcast) {
        this.pastPodcast = pastPodcast;
    }

    public ArrayList<PodcastInputModified> getPastPodcast() {
        return pastPodcast;
    }
}
