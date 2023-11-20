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

    public Like(Command command, LibraryInput library) {
        super(command, library);
    }

    public int verifyLikedSongs(UserHistory user, String nameSong){
        for (SongInput iter : user.getLikedSongs()){
            if (iter.getName().equals(nameSong)) // inseamna ca a mai fost apreciat melodia respectiva
                return 1;
        }
        return 0;
    }

    private SongInput listeningSong(Playlist playlist, int listeningTime) { // vreau sa fac o functie pentru a vedea la ce melodie a ajuns
        if (playlist.getListSongs() != null) {
            int s = playlist.getListSongs().get(0).getSong().getDuration(); // fac un contor pentru a face suma melodiilor si o initializez cu durata primei melodii
            if (listeningTime < s)
                return playlist.getListSongs().get(0).getSong();
            for (int i = 1; i < playlist.getListSongs().size(); i++)
                if (listeningTime > s) // sa vad cand depaseste
                    s += playlist.getListSongs().get(i).getSong().getDuration();
                else
                    return playlist.getListSongs().get(i - 1).getSong();

            if (listeningTime <= s)
                return playlist.getListSongs().get(playlist.getListSongs().size() - 1).getSong();
        }
       return null;
    }

    private void InsertOrRemove(UserHistory user, SongInput song) {
        if (verifyLikedSongs(user, song.getName()) == 0) { // inseamna ca nu a mai fost gasita o melodie
            user.getLikedSongs().add(song);
            this.message = "Like registered successfully.";
        } else { // inseamna ca a mai fost gasit in lista mea si trebuie eliminata
            user.getLikedSongs().remove(song);
            this.message = "Unlike registered successfully.";
        }
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername())); // vreau sa vad userul meu
        if (user.getTimeLoad() != 0) {// sa vad daca a incarcat ceva pana acm
            if (user.getAudioFile().getSongFile() != null) {// inseamnca ca el asculta acum o melodie
                if (user.getLikedSongs().size() == 0) {// inseamna ca nu a apreciat nimic pana acm
                    user.getLikedSongs().add(user.getAudioFile().getSongFile().getSong());
                    this.message = "Like registered successfully.";
                } else { // inseamna ca au mai fost apreciate melodii pana acm
                    InsertOrRemove(user, user.getAudioFile().getSongFile().getSong());
                }
            } else if (user.getAudioFile().getPlaylistFile() != null) { // inseamnca ca am incarcat un playlist si vreau sa vad la ce melodie am ajuns
                    int listeningTime = user.getListeningTime(); // retin sa vad cat a ascultat pana acm
                if (listeningTime == 0) { // daca e 0 inseamnca ca nu am pus niciun pause pana acm (e pe play)
                    int startListening = user.getTimeLoad(); // sa vad cand am incarcat playlistul
                    SongInput song = listeningSong(user.getAudioFile().getPlaylistFile(), getTimestamp() - startListening);
                    if (song != null)
                    InsertOrRemove(user, song);
                } else { // inseamna ca a mai dat play/pause pana acm
                    if (user.isPlayPauseResult()) { // inseamna ca este pe play
                            int moreSeconds = getTimestamp() - user.getTimeLoad(); // retin cat timp a trecut de la ultimul play pana acm
                            int secondsNow = user.getListeningTime(); // ca sa adun timpul de ascultare pana acum cu cat a ascultat in plus
                        SongInput song = listeningSong(user.getAudioFile().getPlaylistFile(), moreSeconds + secondsNow); // adun sa vad cat a ascultat pana acm
                        if (song != null)
                        InsertOrRemove(user, song);

                    } else { // inseamnca ca este pe pauza
                        SongInput song = listeningSong(user.getAudioFile().getPlaylistFile(), listeningTime); // adun sa vad cat a ascultat pana acm
                        if (song != null)
                        InsertOrRemove(user, song);
                    }
                }
            } else {
                this.message = "Loaded source is not a song.";
            }
        } else {
            this.message = "Please load a source before liking or unliking.";
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
