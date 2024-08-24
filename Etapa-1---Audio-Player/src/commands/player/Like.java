package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.Playlist;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import main.Output;

public class Like extends CommandExecute {

    private String message;

    public Like(final Command command, final LibraryInput library) {
        super(command, library);
    }

    private int verifyLikedSongs(final UserHistory user, final String nameSong) {
        for (SongInput iter : user.getLikedSongs()) {
            if (iter.getName().equals(nameSong)) { // inseamna ca a mai fost apreciata melodia
                return 1;
            }
        }
        return 0;
    }

    // vreau sa fac o functie pentru a vedea la ce melodie a ajuns
    private SongInput listeningSong(final Playlist playlist, final int listeningTime) {
        if (playlist.getListSongs() != null) {
            int s = playlist.getListSongs().get(0).getSong().getDuration();
            if (listeningTime < s) {
                return playlist.getListSongs().get(0).getSong();
            }
            for (int i = 1; i < playlist.getListSongs().size(); i++) {
                if (listeningTime > s) {
                    s += playlist.getListSongs().get(i).getSong().getDuration();
                } else {
                    return playlist.getListSongs().get(i - 1).getSong();
                }
            }
            if (listeningTime <= s) {
                return playlist.getListSongs().get(playlist.getListSongs().size() - 1).getSong();
            }
        }
        return null;
    }

    private void insertOrRemove(final UserHistory user, final SongInput song) {
        // verific daca a mai fost gasita melodia
        if (verifyLikedSongs(user, song.getName()) == 0) {
            user.getLikedSongs().add(song);
            this.message = "Like registered successfully.";
        } else { // inseamna ca a mai fost gasit in lista mea si trebuie eliminata
            user.getLikedSongs().remove(song);
            this.message = "Unlike registered successfully.";
        }
    }

    /**
     * aceasta metoda da like unei melodii si o baga in lista de melodii placute a unui user
     * sau unlike daca deja exista in lista si o scoate
     */
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) {
            if (user.getAudioFile().getSongFile() != null) { // inseamnca ca asculta acum o melodie
                if (user.getLikedSongs().size() == 0) { // inseamna ca nu a apreciat nimic pana acm
                    user.getLikedSongs().add(user.getAudioFile().getSongFile().getSong());
                    this.message = "Like registered successfully.";
                } else { // inseamna ca au mai fost apreciate melodii pana acm
                    insertOrRemove(user, user.getAudioFile().getSongFile().getSong());
                }
            } else if (user.getAudioFile().getPlaylistFile() != null) {
                // inseamnca ca am incarcat un playlist si vreau sa vad la ce melodie a ajuns
                int listeningTime = user.getListeningTime();
                if (listeningTime == 0) { // inseamnca ca nu a pus niciun pause pana acm
                    int startListening = user.getTimeLoad(); // sa vad cand am incarcat playlistul
                    SongInput song = listeningSong(user.getAudioFile().getPlaylistFile(),
                            getTimestamp() - startListening);
                    if (song != null) {
                        insertOrRemove(user, song);
                    }
                } else { // inseamna ca a mai dat play/pause pana acm
                    if (user.isPlayPauseResult()) { // inseamna ca este pe play
                        // retin cat timp a trecut de la ultimul play pana acm
                        int moreSeconds = getTimestamp() - user.getTimeLoad();
                        int secondsNow = user.getListeningTime();
                        SongInput song = listeningSong(user.getAudioFile().getPlaylistFile(),
                                moreSeconds + secondsNow); // adun sa vad cat a ascultat pana acm
                        if (song != null) {
                            insertOrRemove(user, song);
                        }

                    } else { // inseamnca ca este pe pauza
                        SongInput song = listeningSong(user.getAudioFile().getPlaylistFile(),
                                listeningTime); // adun sa vad cat a ascultat pana acm
                        if (song != null) {
                            insertOrRemove(user, song);
                        }
                    }
                }
            } else {
                this.message = "Loaded source is not a song.";
            }
        } else {
            this.message = "Please load a source before liking or unliking.";
        }
    }

    /**
     *
     * @return mesajul generat in functia execute
     * daca a fost dat like sau unlike la o melodie
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
