package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInputModified;
import main.Output;

import java.util.Collections;
import java.util.Random;

public class Shuffle extends CommandExecute {

    private int seed;
    private String message;

    public Shuffle(Command command, LibraryInput library, int seed) {
        super(command, library);
        this.seed = seed;
    }

    private void moeveSongsIntoCopy(Playlist playlist) {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        for (SongInputModified iter : playlist.getListSongs())  // fac un for pentru a adauga melodiile
            user.getAudioFile().getPlaylistFile().getCoppiedListSongs().add(iter);// adaug melodiile in lista mea copiata de song uri

    }

    private int returnSumSongsCurrent(Playlist playlist, SongInputModified song) {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        int s = 0;
        for (SongInputModified iter : playlist.getListSongs()) {
            if (iter != song)
                s += iter.getSong().getDuration();
            else
                return s;
        }
        return 0;
    }

//    /*fac o metoda pentru a gasi suma din nooua lista amestecata pana la melodia mea si a o aduna la listeninf time*/
//    private int returnSumSongsModified (Playlist playlist, SongInputModified song) {
//        for (SongInputModified iter : playlist.get)
//    }

    private void moveSongsBack(Playlist playlist) {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        user.getAudioFile().getPlaylistFile().getListSongs().clear();
        for (SongInputModified iter : playlist.getCoppiedListSongs())
            user.getAudioFile().getPlaylistFile().getListSongs().add(iter);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat nimic
            if (user.getAudioFile().getPlaylistFile() != null) { // inseamna ca am incarcat un playlist si =i fac shuffle
                if (user.getAudioFile().getPlaylistFile().isShuffle() == false) { // inseamna ca nu este pe shuffle
                    FindTrack findTrack = new FindTrack(user, getTimestamp()); // vreau sa mi returneze suma episoadelor de pana acm
                    findTrack.findTrackExecute();
                    int newListeningTime = user.getListeningTime() - returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(), findTrack.getSongFound());
                    user.setListeningTime(newListeningTime);

                    moeveSongsIntoCopy(user.getAudioFile().getPlaylistFile()); // copiez lista mea de songurii initiale intr un array
                    Collections.shuffle(user.getAudioFile().getPlaylistFile().getListSongs(), new Random(this.seed)); // fac shuffle pe array listul meu nou generat

                    int addListeningTime = returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(), findTrack.getSongFound());
                    user.setListeningTime(user.getListeningTime() + addListeningTime);
                    // trebuie sa mai adaug suma curenta pana la melodia mea in ordinea in care am pus 0

                    user.getAudioFile().getPlaylistFile().setShuffle(true); // inseamna ca e pe shuffle
                    this.message = "Shuffle function activated successfully.";
                }
                else if (user.getAudioFile().getPlaylistFile().isShuffle() == true) { // inseamna ca este pe shuffle si vreau sa revin la lista initiala
                    FindTrack findTrack = new FindTrack(user, getTimestamp());
                    findTrack.findTrackExecute(); // vreau sa vad la ce melodie a ajuns prin lista mea de melodii shuffled

                    // momentan in user.getPlaylistFile este pastrata lista mea de melodii amestecate
                    // vreau sa vad cat a ascultat pana la melodia mea
                    int newListeningTime = user.getListeningTime() - returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(), findTrack.getSongFound());
                    user.setListeningTime(newListeningTime);

                    moveSongsBack(user.getAudioFile().getPlaylistFile()); // mut in vectorul meu de melodii ordinea initiala

                    int addListeningTime = returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(), findTrack.getSongFound());
                    user.setListeningTime(user.getListeningTime() + addListeningTime);

                    user.getAudioFile().getPlaylistFile().getCoppiedListSongs().clear(); // sterg ce a fost pana acm
                    user.getAudioFile().getPlaylistFile().setShuffle(false); // nu mai este shuffled
                    this.message = "Shuffle function deactivated successfully.";
                }
            } else {
                this.message = "The loaded source is not a playlist.";
            }
        } else {
            this.message = "Please load a source before using the shuffle function.";
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
