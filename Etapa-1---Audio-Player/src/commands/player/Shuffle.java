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

    public Shuffle(final Command command, final LibraryInput library, final int seed) {
        super(command, library);
        this.seed = seed;
    }

    private void moeveSongsIntoCopy(final Playlist playlist) {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        // fac un for pentru a adauga melodiile in copie
        for (SongInputModified iter : playlist.getListSongs()) {
            user.getAudioFile().getPlaylistFile().getCoppiedListSongs().add(iter);
        }
    }

    private int returnSumSongsCurrent(final Playlist playlist, final SongInputModified song) {
        int s = 0;
        for (SongInputModified iter : playlist.getListSongs()) {
            if (iter != song) {
                s += iter.getSong().getDuration();
            } else {
                return s;
            }
        }
        return 0;
    }

    private void moveSongsBack(final Playlist playlist) {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        user.getAudioFile().getPlaylistFile().getListSongs().clear();
        for (SongInputModified iter : playlist.getCoppiedListSongs()) {
            user.getAudioFile().getPlaylistFile().getListSongs().add(iter);
        }
    }

    /**
     * aceasta metoda imi seteaza playlistul pe shuffle si imi amesteca melodiile
     * dupa parametrul seed citit
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            // inseamna ca am incarcat un playlist si i fac shuffle
            if (user.getAudioFile().getPlaylistFile() != null) {
                // inseamna ca nu este pe shuffle
                if (!user.getAudioFile().getPlaylistFile().isShuffle()) {
                    FindTrack findTrack = new FindTrack(user, getTimestamp());
                    findTrack.findTrackExecute();
                    int newListeningTime = user.getListeningTime()
                            - returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(),
                            findTrack.getSongFound());
                    user.setListeningTime(newListeningTime);

                    // copiez lista mea de songurii initiale intr un array
                    moeveSongsIntoCopy(user.getAudioFile().getPlaylistFile());
                    Collections.shuffle(user.getAudioFile().getPlaylistFile().getListSongs(),
                            new Random(this.seed)); // fac shuffle listei de melodii

                    int addListeningTime =
                            returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(),
                                    findTrack.getSongFound());
                    // adaugat timp pana la melodia mea din playlistul amestecat
                    user.setListeningTime(user.getListeningTime() + addListeningTime);

                    user.getAudioFile().getPlaylistFile().setShuffle(true); // e pe shuffle
                    this.message = "Shuffle function activated successfully.";
                } else if (user.getAudioFile().getPlaylistFile().isShuffle()) {
                    // inseamna ca este pe shuffle si vreau sa revin la lista initiala
                    FindTrack findTrack = new FindTrack(user, getTimestamp());
                    findTrack.findTrackExecute();
                    // vreau sa vad la ce melodie a ajuns prin lista mea de melodii shuffled
                    // vreau sa vad cat a ascultat pana la melodia mea
                    int newListeningTime = user.getListeningTime()
                            - returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(),
                            findTrack.getSongFound());
                    user.setListeningTime(newListeningTime);

                    // mut in vectorul meu de melodii ordinea initiala
                    moveSongsBack(user.getAudioFile().getPlaylistFile());

                    int addListeningTime =
                            returnSumSongsCurrent(user.getAudioFile().getPlaylistFile(),
                                    findTrack.getSongFound());
                    user.setListeningTime(user.getListeningTime() + addListeningTime);

                    // sterg ce a fost pana acm stocat
                    user.getAudioFile().getPlaylistFile().getCoppiedListSongs().clear();
                    user.getAudioFile().getPlaylistFile().setShuffle(false);
                    this.message = "Shuffle function deactivated successfully.";
                }
            } else {
                this.message = "The loaded source is not a playlist.";
            }
        } else {
            this.message = "Please load a source before using the shuffle function.";
        }
    }

    /**
     *
     * @return mesajul generat in metoda execute
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
