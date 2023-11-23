package commands;

import fileio.input.SongInput;
import fileio.input.SongInputModified;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Playlist {

    private String namePlaylist;
    private ArrayList<SongInputModified> listSongs = new ArrayList<>();
    private ArrayList<SongInputModified> coppiedListSongs = new ArrayList<>();
    private String typePlaylist; // sa vad daca este de tip public sau privat
    private String user; // vreau sa vad al cui este playlist ul
    private int repeatPlaylist;
    private boolean shuffle = false; // inseamna ca nu este pe shuffle
    private int followers = 0; // vreau sa retin cati oameni dau follow la playlistul

    public Playlist(String namePlaylist, String typePlaylist, String user) {
        this.namePlaylist = namePlaylist;
        this.typePlaylist = typePlaylist;
        this.user = user;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public ArrayList<SongInputModified> getListSongs() {
        return listSongs;
    }

    public String getTypePlaylist() {
        return typePlaylist;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRepeatPlaylist() {
        return repeatPlaylist;
    }

    public void setRepeatPlaylist(int repeatPlaylist) {
        this.repeatPlaylist = repeatPlaylist;
    }

    public void setTypePlaylist(String typePlaylist) {
        this.typePlaylist = typePlaylist;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public ArrayList<SongInputModified> getCoppiedListSongs() {
        return coppiedListSongs;
    }

    public void setCoppiedListSongs(ArrayList<SongInputModified> coppiedListSongs) {
        this.coppiedListSongs = coppiedListSongs;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
