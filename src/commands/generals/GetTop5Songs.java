package commands.generals;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import main.Output;

import java.util.ArrayList;

public class GetTop5Songs extends CommandExecute {

    private ArrayList<LikedSongs> arrayLikedSongs = new ArrayList<>();
    private ArrayList<String> nameLikedSongs = new ArrayList<>();

    public GetTop5Songs(Command command, LibraryInput library) {
        super(command, library);
    }

    private class LikedSongs {
        private SongInput song;
        private int numberLikes;

        public LikedSongs(SongInput song, int numberLikes) {
            this.song = song;
            this.numberLikes = numberLikes;
        }

        public SongInput getSong() {
            return song;
        }

        public void setSong(SongInput song) {
            this.song = song;
        }

        public int getNumberLikes() {
            return numberLikes;
        }

        public void setNumberLikes(int numberLikes) {
            this.numberLikes = numberLikes;
        }
    }

    public int verifyIfExist(String nameSong){
        for (int i = 0; i < arrayLikedSongs.size(); i++)
            if (arrayLikedSongs.get(i).getSong().getName().equals(nameSong))
                return i;
        return -1;
    }

    /**
     * fac o metoda pentru a adauga in lista likedSongs numele tututor melodiilor cu Like
     */
    private void addLikedSongs() {
        for (UserHistory user : getUserHistory()) // vreau sa parcurg toti userii
            for (SongInput iter : user.getLikedSongs()) {
                int indexSongLiked =  verifyIfExist(iter.getName());
                if (indexSongLiked >= 0) { // vreau sa vad daca in arraylist ul meu de clase exista melodia mea
                    arrayLikedSongs.get(indexSongLiked).setNumberLikes(arrayLikedSongs.get(indexSongLiked).getNumberLikes() + 1);
                } else { // inseamna ca nu este continut deci are un singur like momentamn
                    LikedSongs likedSongs = new LikedSongs(iter, 1);
                    this.arrayLikedSongs.add(likedSongs);
                }
            }
    }

    /**
     * fac o metoda pentru a vedea care melodie este prima in library
     */
    private int orderInLibrary(String name) {
        for (int i = 0; i < library.getSongs().size(); i++) {
            if (library.getSongs().get(i).getName().equals(name)) // inseamna ca asta este pozitia
                return i;
        }
        return -1;
    }

    @Override
    public void execute() {
        addLikedSongs();
        for (int i = 0; i <= arrayLikedSongs.size() - 1; i++) { // fac un for pentru a ordona descrescator melodiile dupa nr de like uri
            for (int j = i + 1; j < arrayLikedSongs.size(); j++) {
                if (arrayLikedSongs.get(i).getNumberLikes() < arrayLikedSongs.get(j).numberLikes) {
                    LikedSongs aux = arrayLikedSongs.get(i);
                    arrayLikedSongs.set(i, arrayLikedSongs.get(j));
                    arrayLikedSongs.set(j, aux);
                } else if (arrayLikedSongs.get(i).getNumberLikes() == arrayLikedSongs.get(j).numberLikes) {
                    int indexSong1 = orderInLibrary(arrayLikedSongs.get(i).getSong().getName());
                    int indexSong2 = orderInLibrary(arrayLikedSongs.get(j).getSong().getName());
                    if (indexSong1 > indexSong2) { // daca urmatoarea melodie este inainte o ordonez in functie de ordinea in library
                        LikedSongs aux = arrayLikedSongs.get(i);
                        arrayLikedSongs.set(i, arrayLikedSongs.get(j));
                        arrayLikedSongs.set(j, aux);
                    }
                }
            }
        }
        for (LikedSongs iter : arrayLikedSongs)
            if (nameLikedSongs.size() < 5)
                this.nameLikedSongs.add(iter.getSong().getName());

        if (nameLikedSongs.size() < 5) { // decin inseamna ca trebuie sa mai adaug melodii
            for (SongInput iter : library.getSongs())
                if (nameLikedSongs.size() < 5)
                    if (!nameLikedSongs.contains(iter.getName()))
                        this.nameLikedSongs.add(iter.getName());
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (String iter : this.nameLikedSongs)
            output.getResult().add(iter);
        return output;
    }
}
