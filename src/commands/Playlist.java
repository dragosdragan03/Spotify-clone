package commands;

import fileio.input.SongInputModified;
import java.util.ArrayList;

public class Playlist {

    private String namePlaylist;
    private ArrayList<SongInputModified> listSongs = new ArrayList<>();
    private ArrayList<SongInputModified> coppiedListSongs = new ArrayList<>();
    private String typePlaylist; // sa vad daca este de tip public sau privat
    private String user; // vreau sa vad al cui este playlist ul
    private int repeatPlaylist;
    private boolean shuffle = false; // inseamna ca nu este pe shuffle
    private int followers = 0; // vreau sa retin cati oameni dau follow la playlistul

    public Playlist(final String namePlaylist, final String typePlaylist, final String user) {
        this.namePlaylist = namePlaylist;
        this.typePlaylist = typePlaylist;
        this.user = user;
    }

    /**
     * @return numele playlistului
     */
    public String getNamePlaylist() {
        return namePlaylist;
    }

    /**
     *
     * @return lista de melodii dintr un playlist
     */
    public ArrayList<SongInputModified> getListSongs() {
        return listSongs;
    }

    /**
     * @return tipul playlistului daca este public sau privat
     */
    public String getTypePlaylist() {
        return typePlaylist;
    }

    /**
     * @return numele userului care detine acest playlist
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @return daca este sau nu pe repeat playlistul
     */
    public int getRepeatPlaylist() {
        return repeatPlaylist;
    }

    /**
     *
     * @param repeatPlaylist
     * setez playlistul atunci cand se pune pe repeat
     */
    public void setRepeatPlaylist(final int repeatPlaylist) {
        this.repeatPlaylist = repeatPlaylist;
    }

    /**
     *
     * @param typePlaylist daca este public sau privat si il setez
     */
    public void setTypePlaylist(final String typePlaylist) {
        this.typePlaylist = typePlaylist;
    }

    /**
     *
     * @return sa verific daca playlistul este sau nu pus pe shuffle
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     *
     * @param shuffle
     * atunci cand pun playlistul pe shuffle setez acest camp
     */
    public void setShuffle(final boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * folosesc acest camp doar in shuffle pentru a putea retine ordinea initiala a melodiilor
     * @return lista de melodii in ordinea initiala
     */
    public ArrayList<SongInputModified> getCoppiedListSongs() {
        return coppiedListSongs;
    }

    /**
     *
     * @return numarul de followeri al unui playlist
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * atunci cand un user da follow la un playlist ii dau update acestui camp
     * respectiv daca ii da unfollow
     * @param followers numarul de followeri
     */
    public void setFollowers(final int followers) {
        this.followers = followers;
    }
}
