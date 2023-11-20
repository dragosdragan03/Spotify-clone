package commands;

import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Playlist {

    private String namePlaylist;
    private ArrayList<SongInput> listSongs = new ArrayList<>();
    private String typePlaylist; // sa vad daca este de tip public sau privat
    private String user; // vreau sa vad al cui este playlist ul
    private int listeningPlaylist = 0;

    public Playlist(String namePlaylist, String typePlaylist, String user) {
        this.namePlaylist = namePlaylist;
        this.typePlaylist = typePlaylist;
        this.user = user;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public ArrayList<SongInput> getListSongs() {
        return listSongs;
    }

    public void setListSongs(ArrayList<SongInput> listSongs) {
        this.listSongs = listSongs;
    }

    public String getTypePlaylist() {
        return typePlaylist;
    }

    public void setTypePlaylist(String typePlaylist) {
        this.typePlaylist = typePlaylist;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getListeningPlaylist() {
        return listeningPlaylist;
    }

    public void setListeningPlaylist(int listeningPlaylist) {
        this.listeningPlaylist = listeningPlaylist;
    }
}
