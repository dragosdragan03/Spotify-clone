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

    public GetTop5Songs(final Command command, final LibraryInput library) {
        super(command, library);
    }

    private final class LikedSongs {
        private SongInput song;
        private int numberLikes;

        private LikedSongs(final SongInput song, final int numberLikes) {
            this.song = song;
            this.numberLikes = numberLikes;
        }

        public SongInput getSong() {
            return song;
        }

        public int getNumberLikes() {
            return numberLikes;
        }

        public void setNumberLikes(final int numberLikes) {
            this.numberLikes = numberLikes;
        }
    }

    /**
     * @param nameSong numele melodiei pe care vreau sa o caut in lista de melodii
     * @return indexul melodiei din lista
     */
    public int verifyIfExist(final String nameSong) {
        for (int i = 0; i < arrayLikedSongs.size(); i++) {
            if (arrayLikedSongs.get(i).getSong().getName().equals(nameSong)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * fac o metoda pentru a adauga in lista likedSongs numele tututor melodiilor cu Like
     */
    private void addLikedSongs() {
        for (UserHistory user : getUserHistory()) { // vreau sa parcurg toti userii
            for (SongInput iter : user.getLikedSongs()) {
                int indexSongLiked = verifyIfExist(iter.getName());
                // vreau sa vad daca in arraylist ul meu de clase exista melodia mea
                if (indexSongLiked >= 0) {
                    int numberOfLikes = arrayLikedSongs.get(indexSongLiked).getNumberLikes() + 1;
                    arrayLikedSongs.get(indexSongLiked).setNumberLikes(numberOfLikes);
                } else { // inseamna ca nu este continut deci are un singur like momentamn
                    LikedSongs likedSongs = new LikedSongs(iter, 1);
                    this.arrayLikedSongs.add(likedSongs);
                }
            }
        }
    }

    /**
     * metoda care returneaza indexul din library al unei melodii cautata dupa nume
     */
    private int orderInLibrary(final String name) {
        for (int i = 0; i < library.getSongs().size(); i++) {
            if (library.getSongs().get(i).getName().equals(name)) { // inseamna ca asta este pozitia
                return i;
            }
        }
        return -1;
    }

    /**
     * cauta primele 5 melodii cu cele mai multe likeuri
     */
    @Override
    public void execute() {
        addLikedSongs();
        // fac un for pentru a ordona descrescator melodiile dupa nr de like uri
        for (int i = 0; i <= arrayLikedSongs.size() - 1; i++) {
            for (int j = i + 1; j < arrayLikedSongs.size(); j++) {
                int numberOfLikes1 = arrayLikedSongs.get(i).getNumberLikes();
                int numberOfLikes2 = arrayLikedSongs.get(j).getNumberLikes();
                if (numberOfLikes1 < numberOfLikes2) {
                    LikedSongs aux = arrayLikedSongs.get(i);
                    arrayLikedSongs.set(i, arrayLikedSongs.get(j));
                    arrayLikedSongs.set(j, aux);
                } else if (numberOfLikes1 == numberOfLikes2) {
                    int indexSong1 = orderInLibrary(arrayLikedSongs.get(i).getSong().getName());
                    int indexSong2 = orderInLibrary(arrayLikedSongs.get(j).getSong().getName());
                    // ordonez in functie de ordinea din library
                    if (indexSong1 > indexSong2) {
                        LikedSongs aux = arrayLikedSongs.get(i);
                        arrayLikedSongs.set(i, arrayLikedSongs.get(j));
                        arrayLikedSongs.set(j, aux);
                    }
                }
            }
        }
        final int maxSize = 5;
        for (LikedSongs iter : arrayLikedSongs) {
            if (nameLikedSongs.size() < maxSize) {
                this.nameLikedSongs.add(iter.getSong().getName());
            }
        }

        if (nameLikedSongs.size() < maxSize) { // decin inseamna ca trebuie sa mai adaug melodii
            for (SongInput iter : library.getSongs()) {
                if (nameLikedSongs.size() < maxSize) {
                    if (!nameLikedSongs.contains(iter.getName())) {
                        this.nameLikedSongs.add(iter.getName());
                    }
                }
            }
        }
    }

    /**
     * afiseaza rezultatul
     * @return lista cu cele mai placute 5 melodii
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (String iter : this.nameLikedSongs) {
            output.getResult().add(iter);
        }
        return output;
    }
}
